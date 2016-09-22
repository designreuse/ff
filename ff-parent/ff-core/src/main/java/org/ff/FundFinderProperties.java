package org.ff;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "ff")
@Getter @Setter
public class FundFinderProperties {

	private String dateFormat;

	private String dateTimeFormat;

	private String colatorLocale;

}
