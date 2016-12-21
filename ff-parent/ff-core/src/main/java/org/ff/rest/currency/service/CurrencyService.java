package org.ff.rest.currency.service;

import java.util.ArrayList;
import java.util.List;

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

	@Transactional(readOnly = true)
	public List<CurrencyResource> findAll() {
		List<CurrencyResource> result = new ArrayList<>();
		for (String currencyCode : baseProperties.getCurrencies().split("\\|")) {
			result.add(new CurrencyResource(currencyCode));
		}
		return result;
	}

}
