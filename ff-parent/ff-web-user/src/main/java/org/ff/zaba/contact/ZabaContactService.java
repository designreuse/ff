package org.ff.zaba.contact;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.ff.base.properties.BaseProperties;
import org.ff.rest.contact.resource.ContactResource;
import org.ff.rest.contact.resource.OfficeResource;
import org.ff.zaba.flow.LogClientHttpRequestInterceptor;
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
public class ZabaContactService {

	@Autowired
	private BaseProperties baseProperties;

	@Autowired
	private LogClientHttpRequestInterceptor interceptor;

	@Autowired
	private Collator collator;

	private RestTemplate restTemplate;

	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(interceptor);
		restTemplate.setInterceptors(interceptors);
	}

	public List<OfficeResource> getOffices() {
		List<OfficeResource> result = new ArrayList<>();

		log.debug("Getting offices from external source...");

		try {
			ResponseEntity<ZabaOfficeResource[]> responseEntity = restTemplate.exchange(
					baseProperties.getZabaContactApiOffices(), HttpMethod.GET, new HttpEntity<>(getHttpHeaders()), ZabaOfficeResource[].class);

			for (ZabaOfficeResource resource : responseEntity.getBody()) {
				result.add(new OfficeResource(resource.getId(), null, resource.getGrad(), resource.getPostanskiBroj(), resource.getAdresa(), resource.getVrsta() + " " + resource.getNazivFunkcija().toLowerCase()));
			}

			Collections.sort(result, new Comparator<OfficeResource>() {
				@Override
				public int compare(OfficeResource o1, OfficeResource o2) {
					return collator.compare(o1.getSubdivision2(), o2.getSubdivision2());
				}
			});
		} catch (Exception e) {
			log.error("Getting offices from external source failed", e);
		}

		return result;
	}

	public void submit(ContactResource resource) {
		try {
			log.debug("Submitting contact form: {}", resource);

			ZabaContactFormRequestResource requestResource = new ZabaContactFormRequestResource();
			requestResource.setVrsta(baseProperties.getZabaContactApiSubmitId());
			requestResource.setNazivTvrtke(resource.getCompany().getName());
			requestResource.setOibTvrtke(resource.getCompany().getCode());
			requestResource.setNameKontakt(resource.getName());
			requestResource.setEmailKontakt(resource.getEmail());
			requestResource.setTeleKontakt(resource.getPhone());
			requestResource.setOffice(resource.getLocation().getId() + " " + resource.getLocation().getPrefix()
					+ " " + resource.getLocation().getAddress() + ", " + resource.getLocation().getSubdivision2());
			requestResource.setPoruka(resource.getText());

			MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
			headers.add("Accept", MediaType.toString(Arrays.asList(MediaType.APPLICATION_JSON)));

			HttpEntity<ZabaContactFormRequestResource> request = new HttpEntity<>(requestResource);

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

}
