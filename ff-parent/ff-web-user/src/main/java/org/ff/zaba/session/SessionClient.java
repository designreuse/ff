package org.ff.zaba.session;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.ff.base.properties.BaseProperties;
import org.ff.jpa.domain.ConfigParam.ConfigParamName;
import org.ff.jpa.repository.ConfigParamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.io.BaseEncoding;

import hr.zaba.session.CheckAuthIdRequestType;
import hr.zaba.session.CheckAuthIdResponseType;
import hr.zaba.session.ObjectFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionClient extends WebServiceGatewaySupport {

	@Autowired
	private BaseProperties baseProperties;

	@Autowired
	private ConfigParamRepository configParamRepository;

	private ObjectFactory objectFactory;

	private DocumentBuilder documentBuilder;

	@PostConstruct
	public void init() {
		objectFactory = new ObjectFactory();
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setNamespaceAware(false);
			documentBuilderFactory.setValidating(false);
			documentBuilderFactory.setFeature("http://xml.org/sax/features/namespaces", false);
			documentBuilderFactory.setFeature("http://xml.org/sax/features/validation", false);
			documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
			documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			log.error("Creating document builder failed", e);
		}
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

			data.putAll(parseXmlFromString(new String(BaseEncoding.base64().decode(response.getParams()), "UTF-8")));

			log.debug("Data: {}", data);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			if (Boolean.parseBoolean(configParamRepository.findByName(ConfigParamName.test_mode.toString()).getValue())) {
				try {
					data.put("user_id", "12345678901"); // OIB zastupnika
					data.put("user_id2", "49355429927"); // OIB pravne osobe

					StringBuilder sb = new StringBuilder();
					sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
					sb.append("<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">");
					sb.append("<properties>");
					sb.append("<comment>ezaba-ff transfer</comment>");
					sb.append("<entry key=\"matbr\">00242861-000</entry>");
					sb.append("<entry key=\"suglasnost\">0</entry>");
					sb.append("<entry key=\"ime\">Šime</entry>");
					sb.append("<entry key=\"prezime\">Jakovčić</entry>");
					sb.append("<entry key=\"email\">sime.jakovcic@gmail.com</entry>");
					sb.append("</properties>");

					CheckAuthIdResponseType response = new CheckAuthIdResponseType();
					response.setParams(BaseEncoding.base64().encode(sb.toString().getBytes("UTF-8")));

					data.putAll(parseXmlFromString(new String(BaseEncoding.base64().decode(response.getParams()), "UTF-8")));
				} catch (Exception exc) {
					log.error(exc.getMessage(), exc);
				}
			}
		}

		return data;
	}

	public Map<String, String> parseXmlFromString(String xmlString) {
		Map<String, String> data = new HashMap<>();

		try {
			log.debug("Parsing XML string: {}", xmlString);

			NodeList nodeList = documentBuilder.parse(new ByteArrayInputStream(xmlString.getBytes())).getElementsByTagName("entry");
			log.debug("===");
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
