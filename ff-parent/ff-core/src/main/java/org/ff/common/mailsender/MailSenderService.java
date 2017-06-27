package org.ff.common.mailsender;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;

import org.ff.base.properties.BaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailSenderService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private BaseProperties baseProperties;

	private SendGrid sendGrid;

	@PostConstruct
	public void init() {
		if (Boolean.TRUE == baseProperties.getSendgridEnabled()) {
			log.info("Initializing SendGrid with API key {}", baseProperties.getSendgridApikey());
			sendGrid = new SendGrid(baseProperties.getSendgridApikey());
		}
	}

	public void send(final String to, final String subject, final String text) {
		log.debug("Sending e-mail with subject [{}] to [{}]", subject, to);

		if (Boolean.TRUE == baseProperties.getSendgridEnabled() && sendGrid != null) {
			sendViaSendGrid(to, subject, text);
		} else {
			MimeMessagePreparator preparator = new MimeMessagePreparator() {
				@Override
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
					message.setFrom(baseProperties.getMailSender());
					message.setTo(to);
					message.setSubject(subject);
					message.setText(text, true);
				}
			};

			mailSender.send(preparator);
		}
	}

	public void send(final String[] tos, final String subject, final String text) {
		log.debug("Sending e-mail with subject [{}] to {}", subject, tos);

		if (Boolean.TRUE == baseProperties.getSendgridEnabled() && sendGrid != null) {
			for (String to : tos) {
				sendViaSendGrid(to, subject, text);
			}
		} else {
			MimeMessagePreparator preparator = new MimeMessagePreparator() {
				@Override
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
					message.setFrom(baseProperties.getMailSender());
					message.setTo(tos);
					message.setSubject(subject);
					message.setText(text, true);
				}
			};

			mailSender.send(preparator);
		}
	}

	private void sendViaSendGrid(final String to, final String subject, final String text) {
		try {
			Mail mail = new Mail(new Email(baseProperties.getMailSender()), subject, new Email(to), new Content("text/html", text));

			Request request = new Request();
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sendGrid.api(request);
			if (response.getStatusCode() == 202) {
				log.debug("E-mail successfully sent");
			} else {
				log.debug("Response status code: {}; for details see: https://sendgrid.com/docs/API_Reference/Web_API_v3/Mail/errors.html", response.getStatusCode());
			}
		} catch (Exception e) {
			log.error("Sending e-mail failed", e);
		}
	}

}
