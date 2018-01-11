package org.ff.rest.contact.service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ff.base.properties.BaseProperties;
import org.ff.common.mailsender.MailSenderService;
import org.ff.jpa.domain.ConfigParam.ConfigParamName;
import org.ff.jpa.domain.Contact;
import org.ff.jpa.repository.ConfigParamRepository;
import org.ff.jpa.repository.ContactRepository;
import org.ff.rest.contact.resource.ContactResource;
import org.ff.rest.contact.resource.OfficeResource;
import org.ff.zaba.resource.ZabaContactFormRequestResource;
import org.ff.zaba.resource.ZabaOfficeResource;
import org.ff.zaba.service.ZabaApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class ContactService {

	@Autowired
	private Collator collator;

	@Autowired
	private MailSenderService mailSender;

	@Autowired
	private Configuration configuration;

	@Autowired
	private BaseProperties baseProperties;

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private ZabaApiService zabaApiService;

	@Autowired
	private ConfigParamRepository configParamRepository;

	@Cacheable(value = "locations")
	public List<OfficeResource> getLocations() {
		List<OfficeResource> result = new ArrayList<>();

		for (ZabaOfficeResource resource : zabaApiService.getOffices()) {
			if (resource != null) {
				StringBuffer prefix = new StringBuffer();
				if (StringUtils.isNotBlank(resource.getNazivFunkcija())) {
					prefix.append(resource.getNazivFunkcija());
				}
				result.add(new OfficeResource(resource.getId(), null, resource.getGrad(), resource.getPostanskiBroj(), resource.getAdresa(), prefix.toString().trim()));
			}
		}

		Collections.sort(result, new Comparator<OfficeResource>() {
			@Override
			public int compare(OfficeResource o1, OfficeResource o2) {
				return collator.compare(o1.getSubdivision2(), o2.getSubdivision2());
			}
		});

		return result;
	}

	public ContactResource get() {
		return new ContactResource();
	}

	public void set(ContactResource resource) {
		try {
			Template template = configuration.getTemplate("email_contact.ftl");
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("companyName", resource.getCompany().getName());
			model.put("companyCode", resource.getCompany().getCode());
			model.put("contactName", resource.getName());
			model.put("contactEmail", resource.getEmail());
			model.put("contactPhone", StringUtils.isNotBlank(resource.getPhone()) ? resource.getPhone() : "");
			model.put("location", resource.getLocation().getPrefix()
					+ " " + resource.getLocation().getAddress() + ", " + resource.getLocation().getSubdivision2());
			model.put("text", StringUtils.isNotBlank(resource.getText()) ? resource.getText() : "");

			for (String contactEmail : configParamRepository.findByName(ConfigParamName.contact_email_to.toString()).getValue().split("\\|")) {
				mailSender.send(contactEmail, configParamRepository.findByName(ConfigParamName.contact_email_subject.toString()).getValue(), FreeMarkerTemplateUtils.processTemplateIntoString(template, model));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		Contact contact = new Contact();
		contact.setCompanyName(resource.getCompany().getName());
		contact.setCompanyCode(resource.getCompany().getCode());
		contact.setName(resource.getName());
		contact.setEmail(resource.getEmail());
		contact.setPhone(resource.getPhone());
		contact.setLocation(resource.getLocation().getPrefix()
				+ " " + resource.getLocation().getAddress() + ", " + resource.getLocation().getSubdivision2());
		contact.setText(resource.getText());
		contactRepository.save(contact);

		ZabaContactFormRequestResource requestResource = new ZabaContactFormRequestResource();
		requestResource.setVrsta(baseProperties.getZabaContactApiSubmitId());
		requestResource.setNazivTvrtke(resource.getCompany().getName());
		requestResource.setOibTvrtke(resource.getCompany().getCode());
		requestResource.setNameKontakt(resource.getName());
		requestResource.setEmailKontakt(resource.getEmail());
		requestResource.setTeleKontakt(resource.getPhone());
		requestResource.setOffice(resource.getLocation().getId() + " " + resource.getLocation().getPrefix()
				+ " " + resource.getLocation().getAddress() + ", " + resource.getLocation().getSubdivision2());
		requestResource.setPoruka(resource.getText());

		zabaApiService.submit(requestResource);
	}

}
