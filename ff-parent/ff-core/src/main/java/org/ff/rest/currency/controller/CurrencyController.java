package org.ff.rest.currency.controller;

import java.util.List;

import org.ff.base.controller.BaseController;
import org.ff.rest.currency.resource.CurrencyResource;
import org.ff.rest.currency.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = { "/api/v1/currencies" })
public class CurrencyController extends BaseController {

	@Autowired
	private CurrencyService currencyService;

	@RequestMapping(method = RequestMethod.GET)
	public List<CurrencyResource> findAll() {
		return currencyService.findAll();
	}

}
