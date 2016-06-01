package org.cucina.email.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import org.cucina.email.model.EmailDescriptor;
import org.cucina.email.model.NameValuePair;
import org.cucina.email.service.model.EmailUser;
import org.cucina.email.service.model.SimpleEmailUser;

import reactor.bus.Event;
import reactor.bus.EventBus;

/**
 * Common handling functionality as in building request from @see EmailDto
 *
 * @author vlevine
 */
@Component
public class EmailPreprocessorImpl implements EmailPreprocessor {
	private static final Logger LOG = LoggerFactory.getLogger(EmailPreprocessorImpl.class);

	@Autowired
	private PreparatorEmailService preparatorService;

	@Autowired
	private EventBus eventBus;

	/**
	 * Builds request to emailService and sends result to eventBus
	 *
	 * @param emailDescriptor JAVADOC.
	 */
	@Async
	@Override
	public void sendEmail(EmailDescriptor emailDescriptor) {
		MimeMessagePreparator[] preparators = preparatorService.prepareMessages(
				emailDescriptor.getSubject(), emailDescriptor.getFrom(),
				buildUsers(emailDescriptor.getTo(), emailDescriptor.getLocale()),
				buildUsers(emailDescriptor.getCc(), emailDescriptor.getLocale()),
				buildUsers(emailDescriptor.getBcc(), emailDescriptor.getLocale()),
				emailDescriptor.getMessageKey(), collectionToMap(emailDescriptor.getParameters()),
				null);

		eventBus.notify(AsyncEventSender.class, Event.wrap(preparators));
	}

	private Map<String, String> collectionToMap(Collection<NameValuePair> nvps) {
		Map<String, String> result = new HashMap<String, String>();
		for (NameValuePair nameValuePair : nvps) {
			result.put(nameValuePair.getName(), nameValuePair.getValue());
		}

		return result;
	}

	private Collection<EmailUser> buildUsers(Collection<String> addresses, String slocale) {
		if (CollectionUtils.isEmpty(addresses)) {
			return Collections.emptyList();
		}

		Collection<EmailUser> users = new ArrayList<EmailUser>();

		Locale locale = null;

		if (StringUtils.isNotEmpty(slocale)) {
			try {
				locale = LocaleUtils.toLocale(slocale);
			} catch (Exception e) {
				LOG.error("Oops", e);
			}
		}

		if (locale == null) {
			locale = Locale.getDefault();
		}

		for (String ass : addresses) {
			if (StringUtils.isEmpty(ass)) {
				continue;
			}

			users.add(new SimpleEmailUser(ass.trim(), locale));
		}

		return users;
	}
}
