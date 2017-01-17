package org.ff.zaba.session;

import org.ff.base.properties.BaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SessionConfiguration {

	@Autowired
	private BaseProperties baseProperties;

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan("hr.zaba.session");
		return marshaller;
	}

	@Bean
	public SessionClient sovaClient(Jaxb2Marshaller marshaller) {
		log.debug("Creating SESSION TRANSFER SERVICE client with end-point: {}", baseProperties.getZabaSessionUrl());
		SessionClient client = new SessionClient();
		client.setDefaultUri(baseProperties.getZabaSessionUrl());
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}

}
