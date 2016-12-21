package org.ff.common.etm;

import java.io.StringWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.ff.base.properties.BaseProperties;
import org.ff.common.mailsender.MailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import etm.contrib.renderer.SimpleHtmlRenderer;
import etm.core.configuration.BasicEtmConfigurator;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;
import etm.core.renderer.SimpleTextRenderer;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EtmService {

	private static EtmMonitor etmMonitor;

	@Autowired
	private EtmProperties etmProperties;

	@Autowired
	private BaseProperties ffProperties;

	@Autowired
	private MailSenderService mailSender;

	@Autowired
	private Configuration configuration;

	@PostConstruct
	void init() {
		log.info("Initializing ETM monitor...");
		BasicEtmConfigurator.configure(true);
		etmMonitor = EtmManager.getEtmMonitor();
		if (!etmMonitor.isStarted()) {
			etmMonitor.start();
		} else {
			etmMonitor.reset();
		}
		log.info("ETM monitor successfully started");
	}

	@PreDestroy
	void destroy() {
		if (etmMonitor != null && etmMonitor.isStarted()) {
			etmMonitor.stop();
			log.info("ETM monitor successfully stopped");
		}
	}

	public EtmPoint createPoint(String symbolicName) {
		EtmPoint point = null;
		if (etmMonitor != null && etmMonitor.isStarted() && etmMonitor.isCollecting()) {
			point = etmMonitor.createPoint(symbolicName);
		}
		return point;
	}

	public void collect(EtmPoint point) {
		point.collect();
	}

	public double getTransactionTime(EtmPoint point) {
		if (point != null) {
			return point.getTransactionTime();
		}
		return -1;
	}

	@Scheduled(cron = "${etm.reset-cron-expression}")
	public void reset() {
		if (etmMonitor != null) {
			String subject = String.format("Fund Finder - Overview of measurement points for period [%s] - [%s]",
					DateFormatUtils.format(etmMonitor.getMetaData().getLastResetTime(), ffProperties.getDateTimeFormat()),
					DateFormatUtils.format(Calendar.getInstance().getTime(), ffProperties.getDateTimeFormat()));
			StringWriter stringWriter = new StringWriter();
			etmMonitor.render(new SimpleTextRenderer(stringWriter));
			log.info("{}\n{}", subject, stringWriter.toString());

			if (etmProperties.getSendEmail()) {
				try {
					Template template = configuration.getTemplate("email_etm.ftl");

					stringWriter = new StringWriter();
					etmMonitor.render(new SimpleHtmlRenderer(stringWriter));

					Map<String, Object> model = new HashMap<String, Object>();
					model.put("data", stringWriter.toString());

					mailSender.send(etmProperties.getSendEmailTo(), subject, FreeMarkerTemplateUtils.processTemplateIntoString(template, model));
				} catch (Exception e) {
					log.error("Sending ETM e-mail failed", e);
				}
			}

			etmMonitor.reset();
		}
	}

}
