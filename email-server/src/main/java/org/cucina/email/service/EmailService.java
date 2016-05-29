package org.cucina.email.service;

import java.util.Collection;
import java.util.Map;

import javax.activation.DataSource;


/**
 * JAVADOC.
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public interface EmailService {
    /**
    * JAVADOC.
    *
    * @param descriptor JAVADOC.
    *
    * @return JAVADOC.
    */
    public void sendMessages(String from, String subject, Collection<EmailUser> toUsers,
        Collection<EmailUser> ccUsers, Collection<EmailUser> bccUsers, String messageKey,
        Map<String, String> map, Collection<DataSource> attachments);
}
