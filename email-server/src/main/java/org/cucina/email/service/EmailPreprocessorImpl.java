package org.cucina.email.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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
 * Common handling functionality as in building request from @see EmailDescriptor
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
	 * Builds request in preparatorService and sends result to eventBus
	 *
	 * @param emailDescriptor JAVADOC.
	 */
	@Async
	@Override
	public void sendEmail(EmailDescriptor emailDescriptor) {
		Locale locale = determineLocale(emailDescriptor.getLocale());
		MimeMessagePreparator[] preparators = preparatorService.prepareMessages(
				emailDescriptor.getSubject(), emailDescriptor.getFrom(),
				buildUsers(emailDescriptor.getTo(), locale),
				buildUsers(emailDescriptor.getCc(), locale),
				buildUsers(emailDescriptor.getBcc(), locale), emailDescriptor.getMessageKey(),
				collectionToMap(emailDescriptor.getParameters()), null);

		eventBus.notify(AsyncEventSender.class, Event.wrap(preparators));
	}

	private Map<String, String> collectionToMap(Collection<NameValuePair> nvps) {
		return nvps.parallelStream()
				.collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));
	}

	private Collection<EmailUser> buildUsers(Collection<String> addresses, Locale locale) {
		if (CollectionUtils.isEmpty(addresses)) {
			return Collections.emptyList();
		}

		// TODO verify address format
		Collection<EmailUser> users = addresses.parallelStream().filter(StringUtils::isNotEmpty)
				.map(s -> new SimpleEmailUser(s.trim(), locale)).collect(Collectors.toList());

		return users;
	}

	private Locale determineLocale(String slocale) {
		Locale locale = null;
		if (StringUtils.isNotEmpty(slocale)) {
			try {
				locale = LocaleUtils.toLocale(slocale);
			} catch (Exception e) {
				LOG.warn("Invalid locale '" + slocale + "', will use server's default "
						+ Locale.getDefault(), e);
			}
		}

		if (locale == null) {
			locale = Locale.getDefault();
		}

		return locale;
	}
}
