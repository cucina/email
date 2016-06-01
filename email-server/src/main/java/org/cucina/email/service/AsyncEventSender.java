package org.cucina.email.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import reactor.bus.Event;

@Service
public class AsyncEventSender {
	private static final Logger LOG = LoggerFactory.getLogger(AsyncEventSender.class);

	@Autowired
	private JavaMailSender javaMailSender;

	public void send(Event<MimeMessagePreparator[]> event) {
		MimeMessagePreparator[] preparators = event.getData();
		
		try {
			javaMailSender.send(preparators);
		} catch (MailSendException e) {
			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < preparators.length; i++) {
				sb.append("MessagePreparator=").append(preparators[i]).append("  ");
			}

			LOG.error("Caught exception when sending email:" + sb, e);
		}
	}
}