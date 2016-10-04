package org.ff.properties;

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

	private String colatorLocale;

	private Boolean runBatch;

}
