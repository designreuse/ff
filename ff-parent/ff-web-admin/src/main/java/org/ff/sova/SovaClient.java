package org.ff.sova;

import java.util.List;

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

	public String wsKorisnikAutorizacija(String user) {
		String result = null;

		try {
			log.info("Invoking SOVA method [wsKorisnikAutorizacija] for user [{}]...", user);

			WSKorisnikAutorizacija request = new WSKorisnikAutorizacija();
			request.setArg0(baseProperties.getSovaApplication());
			request.setArg1(user);

			WSKorisnikAutorizacijaResponse response = (WSKorisnikAutorizacijaResponse) getWebServiceTemplate().marshalSendAndReceive(new ObjectFactory().createWSKorisnikAutorizacija(request));

			if (response != null) {
				log.info("Response: {}", response);
				List<WsKorinikAutorizacijaoData> data = response.getReturn();
				if (data != null) {
					log.info("Response data: {}", data);
					for (WsKorinikAutorizacijaoData d : data) {
						if (StringUtils.isNotBlank(d.getFunkcija())) {
							result = d.getFunkcija();
							log.info("Role found: {}", result);
							break;
						}
					}
				} else {
					log.warn("Response data: {}", data);
				}
			} else {
				log.warn("Response: {}", response);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return result;
	}

}
