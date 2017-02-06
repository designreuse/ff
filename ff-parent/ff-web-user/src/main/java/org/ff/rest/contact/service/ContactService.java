package org.ff.rest.contact.service;

import java.util.ArrayList;
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
	private MailSenderService mailSender;

	@Autowired
	private Configuration configuration;

	@Autowired
	private BaseProperties baseProperties;

	@Autowired
	private ContactRepository contactRepository;

	private Map<Integer, String> contactTopics;
	private Map<Integer, String> contactTypes;
	private Map<Integer, String> contactChannels;
	private List<String> contactEmails;

	@PostConstruct
	public void init() {
		contactTopics = new HashMap<>();
		for (String str : baseProperties.getContactTopics().split("\\|")) {
			String[] array = str.split("\\:");
			contactTopics.put(Integer.parseInt(array[0]), array[1]);
		}

		contactTypes = new HashMap<>();
		for (String str : baseProperties.getContactTypes().split("\\|")) {
			String[] array = str.split("\\:");
			contactTypes.put(Integer.parseInt(array[0]), array[1]);
		}

		contactChannels = new HashMap<>();
		for (String str : baseProperties.getContactChannels().split("\\|")) {
			String[] array = str.split("\\:");
			contactChannels.put(Integer.parseInt(array[0]), array[1]);
		}

		contactEmails = new ArrayList<>();
		for (String str : baseProperties.getContactEmailTo().split("\\|")) {
			contactEmails.add(str);
		}
	}

	public List<KeyValueResource> getTopics() {
		List<KeyValueResource> resources = new ArrayList<>();
		for (Entry<Integer, String> entry : contactTopics.entrySet()) {
			resources.add(new KeyValueResource(entry.getKey(), entry.getValue()));
		}
		return resources;
	}

	public List<KeyValueResource> getTypes() {
		List<KeyValueResource> resources = new ArrayList<>();
		for (Entry<Integer, String> entry : contactTypes.entrySet()) {
			resources.add(new KeyValueResource(entry.getKey(), entry.getValue()));
		}
		return resources;
	}

	public List<KeyValueResource> getChannels() {
		List<KeyValueResource> resources = new ArrayList<>();
		for (Entry<Integer, String> entry : contactChannels.entrySet()) {
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
			model.put("topic", resource.getTopic().getValue());
			model.put("channel", resource.getChannel().getValue());
			model.put("type", resource.getType().getValue());
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
		contact.setTopic(resource.getTopic().getValue());
		contact.setType(resource.getType().getValue());
		contact.setChannel(resource.getChannel().getValue());
		contact.setText(resource.getText());
		contactRepository.save(contact);
	}

}
