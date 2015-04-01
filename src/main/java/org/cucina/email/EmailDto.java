package org.cucina.email;

import java.io.Serializable;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
public class EmailDto
    implements Serializable {
    private static final long serialVersionUID = -4534743023328299950L;
    private Map<String, Object> parameters;
    private String bcc;
    private String cc;
    private String from;
    private String locale;
    private String messageKey;
    private String subject;
    private String to;

    /**
    * JAVADOC Method Level Comments
    *
    * @param bcc JAVADOC.
    */
    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getBcc() {
        return bcc;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param cc JAVADOC.
     */
    public void setCc(String cc) {
        this.cc = cc;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getCc() {
        return cc;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param from JAVADOC.
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getFrom() {
        return from;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param locale JAVADOC.
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getLocale() {
        return locale;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param messageKey JAVADOC.
     */
    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param parameters JAVADOC.
     */
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Map<String, Object> getParameters() {
        return parameters;
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
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param to JAVADOC.
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getTo() {
        return to;
    }

    /**
         * Default toString implementation
         *
         * @return This object as String.
         */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
