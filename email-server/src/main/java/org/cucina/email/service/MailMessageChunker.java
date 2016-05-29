package org.cucina.email.service;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.activation.DataSource;

import org.springframework.mail.javamail.MimeMessagePreparator;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface MailMessageChunker {
    /**
     * Sets the chunk size to divide the emails by
     * @param chunkSize
     */
    void setChunkSize(int chunkSize);

    /**
     * Takes a mail descriptor, locale and collections of recipients and chunks
     * emails, creating message preparators which are used by Java Mail
     * @param templateName - name of template
     * @param params - parameters to pass to email generator
     * @param locale - locale for this message
     * @param tos - to recipients
     * @param ccs - cc recipients
     * @param bccs - bcc recipients
     * @return set of MimeMessagePreparator
     */
    Set<MimeMessagePreparator> getPreparators(String templateName, Map<String, String> params,
        Locale locale, Collection<?extends EmailUser> tos, Collection<?extends EmailUser> ccs,
        Collection<?extends EmailUser> bccs, Collection<DataSource> attachments);
}
