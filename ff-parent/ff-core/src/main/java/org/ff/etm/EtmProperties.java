package org.ff.etm;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "etm")
@Getter @Setter
public class EtmProperties {

	private String resetCronExpression;

	private Boolean sendEmail;

	private String sendEmailTo;

}
