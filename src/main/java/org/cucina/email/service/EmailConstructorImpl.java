package org.cucina.email.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.activation.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC.
 *
 * @author $author$
 * @version $Revision: 1.16 $
 * @param <T>
 * @param <T>
 */
@Component
public class EmailConstructorImpl
    implements EmailConstructor {
    private static final Logger LOG = LoggerFactory.getLogger(EmailConstructorImpl.class);
    @Autowired
    private MailMessageChunker mailMessageChunker;

    /**
     * JAVADOC Method Level Comments
     *
     * @param mailMessageChunker
     *            JAVADOC.
     */
    public void setMailMessageChunker(MailMessageChunker mailMessageChunker) {
        this.mailMessageChunker = mailMessageChunker;
    }

    /**
     * Creates preparators for each Locale found in users.
     *
     * @param descriptor
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Transactional
    public MimeMessagePreparator[] prepareMessages(String messageKey,
        Collection<EmailUser> toUsers, Collection<EmailUser> ccUsers,
        Collection<EmailUser> bccUsers, Map<String, Object> parameters,
        Collection<DataSource> attachments) {
        Assert.notNull(messageKey, "messageKey is null");

        Collection<EmailUser> filteredToUsers = filterDodgyUsers(toUsers);

        Collection<EmailUser> filteredCcUsers = filterDodgyUsers(ccUsers);

        Collection<EmailUser> filteredBccUsers = filterDodgyUsers(bccUsers);

        Map<Locale, Collection<EmailUser>> localeTos = sortUsersByLocale(filteredToUsers);

        if (LOG.isDebugEnabled()) {
            LOG.debug("To by locale=" + localeTos);
        }

        Map<Locale, Collection<EmailUser>> localeCcs = sortUsersByLocale(filteredCcUsers);

        if (LOG.isDebugEnabled()) {
            LOG.debug("CC by locale=" + localeCcs);
        }

        Map<Locale, Collection<EmailUser>> localeBccs = sortUsersByLocale(filteredBccUsers);

        if (LOG.isDebugEnabled()) {
            LOG.debug("BCC by locale=" + localeBccs);
        }

        Set<Locale> locales = new HashSet<Locale>();

        locales.addAll(localeTos.keySet());
        locales.addAll(localeCcs.keySet());
        locales.addAll(localeBccs.keySet());

        if (LOG.isDebugEnabled()) {
            LOG.debug("Locales in use [" + locales + "]");
        }

        Collection<MimeMessagePreparator> preparators = new HashSet<MimeMessagePreparator>();

        // If we have locales then we should have some Users
        for (Locale locale : locales) {
            if (CollectionUtils.isEmpty(localeTos.get(locale)) &&
                    CollectionUtils.isEmpty(localeCcs.get(locale)) &&
                    CollectionUtils.isEmpty(localeBccs.get(locale))) {
                continue;
            }

            Collection<EmailUser> tos = localeTos.get(locale);
            Collection<EmailUser> ccs = localeCcs.get(locale);
            Collection<EmailUser> bccs = localeBccs.get(locale);

            preparators.addAll(mailMessageChunker.getPreparators(messageKey, parameters, locale,
                    tos, ccs, bccs, attachments));
        }

        return preparators.toArray(new MimeMessagePreparator[preparators.size()]);
    }

    private Collection<EmailUser> filterDodgyUsers(Collection<EmailUser> users) {
        if (users != null) {
            Collection<EmailUser> ret = new HashSet<EmailUser>();

            for (Iterator<EmailUser> iter = users.iterator(); iter.hasNext();) {
                EmailUser user = iter.next();

                if (user != null) {
                    EmailUser emailUser = user;

                    if (!emailUser.isActive()) {
                        LOG.warn("Removing user from email with username [" +
                            emailUser.getUsername() + "] as it is disabled or deleted");

                        continue;
                    } else if (StringUtils.isEmpty(emailUser.getEmail())) {
                        LOG.warn("Removing user from email with username [" +
                            emailUser.getUsername() + "] as it has no email address");

                        continue;
                    }

                    ret.add(emailUser);
                } else {
                    LOG.error("Users are not User, unexpected");
                }
            }

            return ret;
        }

        return null;
    }

    private Map<Locale, Collection<EmailUser>> sortUsersByLocale(Collection<EmailUser> users) {
        Map<Locale, Collection<EmailUser>> userByLocale = new HashMap<Locale, Collection<EmailUser>>();

        if (users != null) {
            for (EmailUser user : users) {
                Locale locale = user.getLocale();

                if (locale == null) {
                    locale = Locale.getDefault();
                }

                Collection<EmailUser> tu = userByLocale.get(locale);

                if (tu == null) {
                    tu = new HashSet<EmailUser>();
                }

                tu.add(user);
                userByLocale.put(locale, tu);
            }
        }

        return userByLocale;
    }
}
