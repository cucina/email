package org.cucina.email.service;

import java.util.Collection;
import java.util.Map;

import org.springframework.mail.javamail.MimeMessagePreparator;

import org.cucina.email.service.model.EmailUser;

/**
 * Creates MessagePreparators for each locale found in the list of users.
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public interface EmailConstructor {
	/**
	 * JAVADOC.
	 *
	 * @param descriptor JAVADOC.
	 *
	 * @return JAVADOC.
	 */
	public Collection<MimeMessagePreparator> prepareMessages(String messageKey,
			Collection<EmailUser> toUsers, Collection<EmailUser> ccUsers,
			Collection<EmailUser> bccUsers, Map<String, String> parameters);
}
