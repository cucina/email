package org.cucina.email.service;

import java.util.Collection;
import java.util.Map;

import javax.activation.DataSource;

import org.springframework.mail.javamail.MimeMessagePreparator;

import org.cucina.email.service.model.EmailUser;


/**
 * JAVADOC.
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public interface PreparatorEmailService {
    /**
    * JAVADOC.
    *
    * @param descriptor JAVADOC.
    *
    * @return JAVADOC.
    */
    public MimeMessagePreparator[] prepareMessages(String from, String subject, Collection<EmailUser> toUsers,
        Collection<EmailUser> ccUsers, Collection<EmailUser> bccUsers, String messageKey,
        Map<String, String> map, Collection<DataSource> attachments);
}
