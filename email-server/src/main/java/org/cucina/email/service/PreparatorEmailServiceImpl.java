package org.cucina.email.service;

import java.util.Collection;
import java.util.Map;

import javax.activation.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import org.cucina.email.service.model.EmailUser;

/**
 * Service which sends emails to the intended recipients.
 *
 * @author $author$
 * @version $Revision: 1.16 $
 * @param <T>
 * @param <T>
 */
@Component
public class PreparatorEmailServiceImpl implements PreparatorEmailService {
	private static final Logger LOG = LoggerFactory.getLogger(PreparatorEmailServiceImpl.class);

	@Autowired
	private EmailConstructor emailConstructor;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param messageKey JAVADOC.
	 * @param toUsers JAVADOC.
	 * @param ccUsers JAVADOC.
	 * @param bccUsers JAVADOC.
	 * @param parameters JAVADOC.
	 * @param attachments JAVADOC.
	 */
	@Override
	public MimeMessagePreparator[] prepareMessages(String subject, String from,
			Collection<EmailUser> toUsers, Collection<EmailUser> ccUsers,
			Collection<EmailUser> bccUsers, String messageKey, Map<String, String> parameters,
			Collection<DataSource> attachments) {
		Collection<MimeMessagePreparator> preparators = emailConstructor.prepareMessages(messageKey,
				toUsers, ccUsers, bccUsers, parameters);

		return preparators.parallelStream().map(p -> (MimeMessagePreparatorImpl) p).map(p -> {
			p.setFrom(from);
			p.setSubject(subject);
			p.setAttachments(attachments);
			LOG.debug("Preparator:{}", p);
			return p;
		}).toArray(size -> new MimeMessagePreparator[size]);
	}
}
