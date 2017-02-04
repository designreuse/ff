package org.ff.rest.currency.service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.ff.base.properties.BaseProperties;
import org.ff.base.service.BaseService;
import org.ff.rest.currency.resource.CurrencyResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CurrencyService extends BaseService {

	@Autowired
	private BaseProperties baseProperties;

	private DecimalFormat format;

	@PostConstruct
	public void init() {
		format = new DecimalFormat("###,###.00", new DecimalFormatSymbols(new Locale(baseProperties.getLocale())));
	}

	@Transactional(readOnly = true)
	public List<CurrencyResource> findAll() {
		List<CurrencyResource> result = new ArrayList<>();
		for (String currencyCode : baseProperties.getCurrencies().split("\\|")) {
			result.add(new CurrencyResource(currencyCode));
		}
		return result;
	}

	public String format(Object value, String currencyCode) {
		return (value != null) ? (format.format(Double.parseDouble(value.toString())) + " " + currencyCode) : null;
	}

}
