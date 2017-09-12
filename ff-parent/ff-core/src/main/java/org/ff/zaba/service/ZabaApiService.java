package org.ff.zaba.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.ff.base.properties.BaseProperties;
import org.ff.zaba.resource.LogClientHttpRequestInterceptor;
import org.ff.zaba.resource.ZabaCompanyResource;
import org.ff.zaba.resource.ZabaContactFormRequestResource;
import org.ff.zaba.resource.ZabaContactFormResponseResource;
import org.ff.zaba.resource.ZabaOfficeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ZabaApiService {

	@Autowired
	private BaseProperties baseProperties;

	@Autowired
	private LogClientHttpRequestInterceptor interceptor;

	private RestTemplate restTemplate;

	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(interceptor);
		restTemplate.setInterceptors(interceptors);
	}

	/**
	 * Method returns company data for given combination of company number and branch office number.
	 * @param matbr
	 * @return
	 * @throws Exception
	 */
	public ZabaCompanyResource getCompanyData(String matbr) throws Exception {
		log.debug("Getting company data for [{}] from external source", matbr);

		String companyNumber = null;
		String branchOfficeNumber = null;

		if (matbr.contains("-")) {
			String[] array = matbr.split("\\-");
			companyNumber = array[0];
			branchOfficeNumber = array[1];
		} else {
			companyNumber = matbr;
			branchOfficeNumber = "";
		}

		log.debug("Getting company data for company number [{}] and branch office number [{}]...", companyNumber, branchOfficeNumber);
		ResponseEntity<ZabaCompanyResource> responseEntity = restTemplate.exchange(baseProperties.getZabaApiGetByCompanyNumber(),
				HttpMethod.GET, new HttpEntity<>(getHttpHeaders()), ZabaCompanyResource.class, getUriVariables(companyNumber, branchOfficeNumber));

		return responseEntity.getBody();
	}

	public List<ZabaOfficeResource> getOffices() {
		List<ZabaOfficeResource> result = new ArrayList<>();

		try {
			log.debug("Getting offices from external source...");

			ResponseEntity<ZabaOfficeResource[]> responseEntity = restTemplate.exchange(
					baseProperties.getZabaContactApiOffices(), HttpMethod.GET, new HttpEntity<>(getHttpHeaders()), ZabaOfficeResource[].class);

			for (ZabaOfficeResource resource : responseEntity.getBody()) {
				if (resource.getIdFunkcija() != null && resource.getIdFunkcija().intValue() == 4) {
					result.add(resource);
				}
			}

			log.debug("Offices successfully retrieved from external source: {}", result);
		} catch (Exception e) {
			log.error("Getting offices from external source failed", e);
		}

		return result;
	}

	public void submit(ZabaContactFormRequestResource resource) {
		try {
			log.debug("Submitting contact form: {}", resource);

			MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
			headers.add("Accept", MediaType.toString(Arrays.asList(MediaType.APPLICATION_JSON)));

			HttpEntity<ZabaContactFormRequestResource> request = new HttpEntity<>(resource);

			ResponseEntity<ZabaContactFormResponseResource> responseEntity = restTemplate.exchange(
					baseProperties.getZabaContactApiSubmit(), HttpMethod.POST, request, ZabaContactFormResponseResource.class);

			ZabaContactFormResponseResource responseResource = responseEntity.getBody();

			log.debug("Response: {}", responseResource);
		} catch (Exception e) {
			log.error("Submitting contact form failed", e);
		}
	}

	private HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		return headers;
	}

	private Map<String, String> getUriVariables(String companyNumber, String branchOfficeNumber) {
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("companyNumber", companyNumber);
		uriVariables.put("branchOfficeNumber", branchOfficeNumber);
		return uriVariables;
	}

}
