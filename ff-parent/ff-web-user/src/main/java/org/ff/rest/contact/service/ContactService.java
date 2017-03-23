package org.ff.rest.contact.service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.ff.base.properties.BaseProperties;
import org.ff.common.mailsender.MailSenderService;
import org.ff.common.resource.KeyValueResource;
import org.ff.jpa.domain.Contact;
import org.ff.jpa.repository.ContactRepository;
import org.ff.rest.contact.resource.ContactResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
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

	private Map<Integer, String> contactLocations;

	private List<String> contactEmails;

	@PostConstruct
	public void init() {
		contactLocations = new HashMap<>();
		List<String> locations = Arrays.asList(baseProperties.getContactLocations().split("\\|"));

		Collections.sort(locations, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return collator.compare(o1, o2);
			}
		});

		int i = 1;
		for (String location : locations) {
			contactLocations.put(i++, location);
		}

		contactEmails = new ArrayList<>();
		for (String str : baseProperties.getContactEmailTo().split("\\|")) {
			contactEmails.add(str);
		}
	}

	public List<KeyValueResource> getLocations() {
		List<KeyValueResource> resources = new ArrayList<>();
		for (Entry<Integer, String> entry : contactLocations.entrySet()) {
			resources.add(new KeyValueResource(entry.getKey(), entry.getValue()));
		}
		return resources;
	}

	public ContactResource get(UserDetails principal) {
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
			model.put("location", resource.getLocation().getValue());
			model.put("text", StringUtils.isNotBlank(resource.getText()) ? resource.getText() : "");

			mailSender.send(contactEmails.toArray(new String[contactEmails.size()]), baseProperties.getContactEmailSubject(),
					FreeMarkerTemplateUtils.processTemplateIntoString(template, model));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		Contact contact = new Contact();
		contact.setCompanyName(resource.getCompany().getName());
		contact.setCompanyCode(resource.getCompany().getCode());
		contact.setName(resource.getName());
		contact.setEmail(resource.getEmail());
		contact.setPhone(resource.getPhone());
		contact.setLocation(resource.getLocation().getValue());
		contact.setText(resource.getText());
		contactRepository.save(contact);
	}

}
