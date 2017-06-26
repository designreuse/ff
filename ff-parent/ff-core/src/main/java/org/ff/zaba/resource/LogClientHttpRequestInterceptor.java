package org.ff.zaba.resource;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		traceRequest(request, body);
		ClientHttpResponse clientHttpResponse = execution.execute(request, body);
		traceResponse(clientHttpResponse);
		return clientHttpResponse;
	}

	private void traceRequest(HttpRequest request, byte[] body) throws IOException {
		log.debug("Request URI: " + request.getURI());
		log.debug("Request method: " + request.getMethod());
		log.debug("Request headers: " + request.getHeaders());
	}

	private void traceResponse(ClientHttpResponse response) throws IOException {
		log.debug("Response status code: " + response.getStatusCode());
		log.debug("Response status text: " + response.getStatusText());
		log.debug("Response headers: " + response.getHeaders());
	}

}
