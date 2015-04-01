package org.cucina.email.service;

import java.util.Locale;


class SimpleEmailUser
    implements EmailUser {
    private Locale locale;
    private String email;

    /**
     * Creates a new EmailUserImpl object.
     *
     * @param email JAVADOC.
     * @param slocale JAVADOC.
     */
    public SimpleEmailUser(String email, Locale locale) {
        this.email = email;
        this.locale = (locale == null) ? Locale.getDefault() : locale;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isActive() {
        return true;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public String getEmail() {
        return email;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public Locale getLocale() {
        return locale;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public String getUsername() {
        return null;
    }
}
