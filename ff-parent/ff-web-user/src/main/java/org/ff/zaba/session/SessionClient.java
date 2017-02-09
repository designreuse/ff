package org.ff.zaba.session;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBElement;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ff.base.properties.BaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import hr.zaba.session.CheckAuthIdRequestType;
import hr.zaba.session.CheckAuthIdResponseType;
import hr.zaba.session.ObjectFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionClient extends WebServiceGatewaySupport {

	@Autowired
	private BaseProperties baseProperties;

	private ObjectFactory objectFactory;

	@PostConstruct
	public void init() {
		objectFactory = new ObjectFactory();
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> checkAuthId(String authId) {
		Map<String, String> data = new HashMap<>();

		try {
			log.info("Invoking SESSION TRANSFER SERVICE method [checkAuthId] for [{}]...", authId);

			CheckAuthIdRequestType checkAuthIdRequestType = objectFactory.createCheckAuthIdRequestType();
			checkAuthIdRequestType.setAuthId(authId);
			checkAuthIdRequestType.setDestApp(baseProperties.getZabaSessionApplication());

			log.debug("Request arguments - authId: {}, destApp: {}", authId, baseProperties.getZabaSessionApplication());

			Object object = getWebServiceTemplate().marshalSendAndReceive(objectFactory.createCheckAuthIdRequest(checkAuthIdRequestType));
			log.debug("Object: {}", object);

			JAXBElement<CheckAuthIdResponseType> element = (JAXBElement<CheckAuthIdResponseType>) object;
			log.debug("Element: {}", element);

			CheckAuthIdResponseType response = element.getValue();
			log.debug("Response: {}", response);

			data.putAll(parseXmlFromString(response.getParams()));

			log.debug("Data: {}", data);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			if (baseProperties.getTestMode() == Boolean.TRUE) {
				data.put("user_id", "12345678901"); // OIB zastupnika
				data.put("user_id2", "49355429927"); // OIB pravne osobe

				CheckAuthIdResponseType response = new CheckAuthIdResponseType();
				StringBuilder sb = new StringBuilder();
				sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				sb.append("<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">");
				sb.append("<properties>");
				sb.append("<comment>ezaba-ff transfer</comment>");
				sb.append("<entry key=\"matbr\">00242861-000</entry>");
				sb.append("<entry key=\"suglasnost\">0</entry>");
				sb.append("<entry key=\"ime\">John</entry>");
				sb.append("<entry key=\"prezime\">Doe</entry>");
				sb.append("<entry key=\"email\">john.doe@gmail.com</entry>");
				sb.append("</properties>");
				response.setParams(sb.toString());

				data.putAll(parseXmlFromString(response.getParams()));
			}
		}

		return data;
	}

	public Map<String, String> parseXmlFromString(String xmlString) {
		Map<String, String> data = new HashMap<>();

		try {
			log.debug("Parsing XML string: {}", xmlString);

			NodeList nodeList = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(xmlString.getBytes())).getElementsByTagName("entry");
			for (int i=0; i<nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					data.put(element.getAttribute("key"), element.getTextContent());
				}
			}

			log.debug("Parsing result: {}", data);
		} catch (Exception e) {
			log.error("Parsing XML string failed", e);
		}

		return data;
	}

}
