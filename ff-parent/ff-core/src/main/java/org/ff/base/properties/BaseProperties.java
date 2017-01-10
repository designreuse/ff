package org.ff.base.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "base")
@Getter @Setter
public class BaseProperties {

	private String monthFormat;

	private String dateFormat;

	private String dateTimeFormat;

	private String currencies;

	private String locale;

	private String colatorLocale;

	private String url;

	private String sovaUrl;

	private String sovaApplication;

	private String contactTopics;

	private String contactTypes;

	private String contactChannels;

	private String contactEmailTo;

	private String contactEmailSubject;

}
