package org.ff.common.mailsender;

import javax.mail.internet.MimeMessage;

import org.ff.base.properties.BaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailSenderService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private BaseProperties baseProperties;

	public void send(final String to, final String subject, final String text) {
		log.debug("Sending e-mail with subject [{}] to [{}]", subject, to);

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

	public void send(final String[] to, final String subject, final String text) {
		log.debug("Sending e-mail with subject [{}] to {}", subject, to);

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
