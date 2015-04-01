package org.cucina.email.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.activation.DataSource;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author ajoffe
 *
 */
public class MimeMessagePreparatorImpl
    implements MimeMessagePreparator {
    private static final Logger LOG = LoggerFactory.getLogger(MimeMessagePreparatorImpl.class);
    private Collection<DataSource> attachments;
    private Collection<?extends EmailUser> bcc;
    private Collection<?extends EmailUser> cc;
    private Collection<?extends EmailUser> to;
    private Configuration configuration;
    private Locale locale;
    private Map<String, Object> params;
    private String encoding = "UTF-8";
    private String from;
    private String subject;
    private String suffix;
    private String templateLocation;
    private String templateName;

    /**
     * Set attachments to add to email
     *
     * @param attachments Collection<Datasource>
     */
    public void setAttachments(Collection<DataSource> attachments) {
        this.attachments = attachments;
    }

    /**
     * @param bcc The bcc to set.
     */
    public void setBcc(Collection<?extends EmailUser> bcc) {
        this.bcc = bcc;
    }

    /**
     * @param cc The cc to set.
     */
    public void setCc(Collection<?extends EmailUser> cc) {
        this.cc = cc;
    }

    /**
    * @param configuration The configuration to set
    */
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * JAVADOC.
     *
     * @param encoding JAVADOC.
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * @param from The from to set.
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
    * @param locale The locale to set.Set by user
    */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * JAVADOC.
     *
     * @param params JAVADOC.
     */
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param subject JAVADOC.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Set suffix for template files
     *
     * @param suffix String
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * Set templateName
     *
     * @param templateName String
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * @param to The to to set.
     */
    public void setTo(Collection<?extends EmailUser> to) {
        this.to = to;
    }

    /**
    * JAVADOC.
    *
    * @param mimeMessage JAVADOC.
    *
    * @throws Exception JAVADOC.
    */
    @Override
    public void prepare(MimeMessage mimeMessage)
        throws MessagingException {
        Assert.notNull(templateName, "templateName is null");
        Assert.notNull(encoding, "encoding is null");
        Assert.notNull(configuration, "configuration is null");
        Assert.notNull(suffix, "suffix is null");
        Assert.notNull(params, "params cannot be null");

        if ((CollectionUtils.isEmpty(to) && CollectionUtils.isEmpty(cc) &&
                CollectionUtils.isEmpty(bcc)) || StringUtils.isEmpty(from)) {
            LOG.error("To, cc and bcc are not set or from is not set" + this);
            throw new IllegalArgumentException("To, cc and bcc are not set or from is not set");
        }

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                CollectionUtils.isNotEmpty(attachments), encoding);

        helper.setFrom(from);
        helper.setSubject((subject == null) ? "" : subject);

        if (CollectionUtils.isNotEmpty(to)) {
            for (EmailUser user : to) {
                if (StringUtils.isNotEmpty(user.getEmail())) {
                    try {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Adding to recipient with email address:" + user.getEmail());
                        }

                        helper.addTo(user.getEmail());
                    } catch (MessagingException e) {
                        LOG.warn("Invalid email address: [" + user.getEmail() + "]");

                        continue;
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(cc)) {
            for (EmailUser user : cc) {
                if (StringUtils.isNotEmpty(user.getEmail())) {
                    try {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Adding cc recipient with email address:" + user.getEmail());
                        }

                        helper.addCc(user.getEmail());
                    } catch (MessagingException e) {
                        LOG.warn("Invalid email address: [" + user.getEmail() + "]");

                        continue;
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(bcc)) {
            for (EmailUser user : bcc) {
                if (StringUtils.isNotEmpty(user.getEmail())) {
                    try {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Adding bcc recipient with email address:" + user.getEmail());
                        }

                        helper.addBcc(user.getEmail());
                    } catch (MessagingException e) {
                        LOG.warn("Invalid email address: [" + user.getEmail() + "]");

                        continue;
                    }
                }
            }
        }

        //double check we have at least one valid address!
        Address[] taddresses = mimeMessage.getRecipients(Message.RecipientType.TO);
        Address[] caddresses = mimeMessage.getRecipients(Message.RecipientType.CC);
        Address[] baddresses = mimeMessage.getRecipients(Message.RecipientType.BCC);

        if (((null == taddresses) || (taddresses.length == 0)) &&
                ((null == caddresses) || (caddresses.length == 0)) &&
                ((null == baddresses) || (baddresses.length == 0))) {
            LOG.error("Preparator=" + this);
            throw new IllegalArgumentException("No valid email addresses found");
        }

        if (locale == null) {
            LOG.warn("Locale is null, using default");
            locale = Locale.getDefault();
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Building message for locale=" + locale);
        }

        String messageText = constructByLocale(templateName, params, locale);

        Assert.notNull(messageText, "messageText is null");

        if (LOG.isDebugEnabled()) {
            LOG.debug("Message text:" + messageText);
        }

        resolveSubjectAndBody(messageText, helper);

        if (CollectionUtils.isNotEmpty(attachments)) {
            for (DataSource attachment : attachments) {
                helper.addAttachment(attachment.getName(), attachment);
            }
        }

        mimeMessage.saveChanges();

        if (LOG.isDebugEnabled()) {
            LOG.debug("MimeMessage=" + mimeMessage);
        }
    }

    /**
     * JAVADOC.
     *
     * @return JAVADOC.
     */
    public String toString() {
        return new ToStringBuilder(this).append("locale", locale).append("to", to).append("cc", cc)
                                        .append("bcc", bcc).append("from", from)
                                        .append("templateLocation", templateLocation).toString();
    }

    private String constructByLocale(String messageKey, Map<String, Object> parameters,
        Locale locale) {
        Assert.notNull(messageKey, "messageKey is null");
        Assert.notNull(locale, "locale is null");

        String filename = messageKey + suffix;

        if (LOG.isDebugEnabled()) {
            LOG.debug("filename=" + filename);
            LOG.debug("parameters for freemarker=" + parameters);
        }

        try {
            return FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate(
                    filename, locale, encoding), parameters);
        } catch (IOException e) {
            LOG.warn("Got an IOexception", e);
            throw new MailPreparationException(e);
        } catch (TemplateException e) {
            LOG.warn("Got a Freemarker template exception", e);
            throw new MailPreparationException(e);
        }
    }

    private static void resolveSubjectAndBody(String input, MimeMessageHelper helper)
        throws MessagingException {
        if (null == input) {
            return;
        }

        BufferedReader sr = new BufferedReader(new StringReader(input));
        String firstLine;

        try {
            firstLine = sr.readLine();
        } catch (IOException e) {
            LOG.error("Oops", e);

            return;
        }

        String subject = null;

        if (firstLine.contains("Subject:")) {
            subject = StringUtils.substringAfter(firstLine, "Subject:");
        }

        if (StringUtils.isNotEmpty(subject)) {
            helper.setSubject(subject);
            helper.setText(StringUtils.substringAfter(input, firstLine), true);
        } else {
            helper.setText(input, true);
        }
    }
}
