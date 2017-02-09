package org.ff.base.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "base")
@Getter @Setter
public class BaseProperties {

	private Boolean testMode = Boolean.FALSE;

	private String monthFormat;

	private String dateFormat;

	private String dateTimeFormat;

	private String currencies;

	private String locale;

	private String colatorLocale;

	private String url;

	private String mailSender;

	private String contactTopics;

	private String contactTypes;

	private String contactChannels;

	private String contactEmailTo;

	private String contactEmailSubject;

	private String zabaSovaUrl;

	private String zabaSovaApplication;

	private String zabaSessionUrl;

	private String zabaSessionApplication;

	private String zabaApiGetByCompanyNumber;

	private String mappingLegalTypeNumber;

	private Integer mappingZipCode;

	private Integer mappingFoundingDate;

	private String mappingBankruptcyProcedure;

	private String mappingBlocked5Days;

	private String mappingBlocked20Days;

	private Integer mappingLastYearIncome;

	private Integer mappingNumberOfEmployees;

	private String mappingProfitBeforeTax;

	private String mappingProfitOrLoss;

	private String mappingCapitalTotalLiabilities;

}
