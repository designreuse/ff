package org.ff.zaba.sova;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBElement;

import org.apache.commons.lang3.StringUtils;
import org.ff.base.properties.BaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import hr.zaba.sova.ObjectFactory;
import hr.zaba.sova.WSKorisnikAutorizacija;
import hr.zaba.sova.WSKorisnikAutorizacijaResponse;
import hr.zaba.sova.WsKorinikAutorizacijaoData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SovaClient extends WebServiceGatewaySupport {

	@Autowired
	private BaseProperties baseProperties;

	private ObjectFactory objectFactory;

	@PostConstruct
	public void init() {
		objectFactory = new ObjectFactory();
	}

	@SuppressWarnings("unchecked")
	public String wsKorisnikAutorizacija(String user) {
		String result = null;

		try {
			log.info("Invoking SOVA SERVICE method [wsKorisnikAutorizacija] for user [{}]...", user);

			WSKorisnikAutorizacija request = new WSKorisnikAutorizacija();
			request.setArg0(baseProperties.getZabaSovaApplication());
			request.setArg1(user);

			log.debug("Request arguments - arg0: {}, arg1: {}", baseProperties.getZabaSovaApplication(), user);

			Object object = getWebServiceTemplate().marshalSendAndReceive(objectFactory.createWSKorisnikAutorizacija(request));
			log.debug("Object: {}", object);

			JAXBElement<WSKorisnikAutorizacijaResponse> element = (JAXBElement<WSKorisnikAutorizacijaResponse>) object;
			log.debug("Element: {}", element);

			WSKorisnikAutorizacijaResponse response = element.getValue();
			log.debug("Response: {}", response);

			List<WsKorinikAutorizacijaoData> data = response.getReturn();
			log.info("Data: {}", data);

			for (WsKorinikAutorizacijaoData d : data) {
				if (StringUtils.isNotBlank(d.getFunkcija())) {
					result = d.getFunkcija();
					log.debug("Role found: {}", result);
				}
			}

			log.info("Result: {}", result);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return result;
	}

}
