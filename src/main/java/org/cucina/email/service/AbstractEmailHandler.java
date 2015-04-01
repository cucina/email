package org.cucina.email.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;

import org.cucina.email.EmailDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Common handling functionality as in building request from @see EmailDto
 *
 * @author vlevine
 */
public abstract class AbstractEmailHandler {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractEmailHandler.class);
    @Autowired
    private EmailService emailService;

    /**
     * Builds request to emailService.
     *
     * @param dto
     *            JAVADOC.
     */
    protected void sendEmail(EmailDto dto) {
        try {
            emailService.sendMessages(dto.getSubject(), dto.getFrom(),
                buildUsers(dto.getTo(), dto.getLocale()), buildUsers(dto.getCc(), dto.getLocale()),
                buildUsers(dto.getBcc(), dto.getLocale()), dto.getMessageKey(),
                dto.getParameters(), null);
        } catch (Exception e) {
            LOG.error("Error sending email", e);
        }
    }

    private Collection<EmailUser> buildUsers(String addresses, String slocale) {
        if (StringUtils.isEmpty(addresses)) {
            return Collections.emptyList();
        }

        Collection<EmailUser> users = new ArrayList<EmailUser>();

        if (StringUtils.isEmpty(addresses)) {
            return users;
        }

        String[] ass = addresses.split(",");
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

        for (int i = 0; i < ass.length; i++) {
            if (StringUtils.isEmpty(ass[i])) {
                continue;
            }

            users.add(new SimpleEmailUser(ass[i].trim(), locale));
        }

        return users;
    }
}
