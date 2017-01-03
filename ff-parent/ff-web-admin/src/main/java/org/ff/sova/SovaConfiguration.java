package org.ff.sova;

import org.ff.base.properties.BaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SovaConfiguration {

	@Autowired
	private BaseProperties baseProperties;

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan("hr.zaba.sova");
		return marshaller;
	}

	@Bean
	public SovaClient sovaClient(Jaxb2Marshaller marshaller) {
		log.debug("Creating SOVA client with end-point: {}", baseProperties.getSovaUrl());
		SovaClient client = new SovaClient();
		client.setDefaultUri(baseProperties.getSovaUrl());
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}

}
