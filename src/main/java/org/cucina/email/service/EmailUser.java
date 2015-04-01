package org.cucina.email.service;

import java.util.Locale;


/**
 * Third party Users should implement this in order to use email functionality.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface EmailUser {
    /**
     *Is this User active
     *
     * @return is active.
     */
    boolean isActive();

    /**
     * Get emailAddress
     *
     * @return String User's emailAddress.
     */
    String getEmail();

    /**
     * Get locale of the User
     *
     * @return User's locale.
     */
    Locale getLocale();

    /**
     * Get the User's username if it has one
     *
     * @return String username.
     */
    String getUsername();
}
