package org.cucina.email.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import org.springframework.mail.javamail.MimeMessagePreparator;

import org.cucina.email.service.EmailConstructorImpl;
import org.cucina.email.service.EmailUser;
import org.cucina.email.service.MailMessageChunker;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC.
 *
 * @author $author$
 * @version $Revision$
 */
public class ConstructEmailServiceImplTest {
    private EmailConstructorImpl emailService;
    private EmailUser activeWithEmailFrench;
    private EmailUser activeWithoutEmailFrench;
    private EmailUser disabledWithEmailFrench;
    private EmailUser disabledWithoutEmailFrench;
    private EmailUser english;
    @Mock
    private MailMessageChunker chunker;
    @Mock
    private MimeMessagePreparator preparator;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        activeWithEmailFrench = new LocalUser(true, "ajoffe@cucina.org", Locale.FRENCH, "a");
        disabledWithEmailFrench = new LocalUser(true, "thornton@cucina.org", Locale.FRENCH, "t");
        activeWithoutEmailFrench = new LocalUser(true, "vlevine@cucina.org", Locale.FRENCH, "v");
        disabledWithoutEmailFrench = new LocalUser(true, "hkelsey@cucina.org", Locale.FRENCH, "h");
        english = new LocalUser(true, "jnguyen@cucina.org", Locale.ENGLISH, "j");

        emailService = new EmailConstructorImpl();
        emailService.setMailMessageChunker(chunker);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void testBCC() {
        Collection<EmailUser> users = new ArrayList<EmailUser>();

        users.add(activeWithEmailFrench);
        users.add(disabledWithEmailFrench);
        users.add(activeWithoutEmailFrench);
        users.add(disabledWithoutEmailFrench);
        when(chunker.getPreparators(eq("APOverdue"), eq((Map<String, String>) null),
                eq(Locale.FRENCH), eq((Collection) null), eq((Collection) null),
                any(Collection.class), eq((Collection) null)))
            .thenReturn(Collections.singleton(preparator));

        MimeMessagePreparator[] preparators = emailService.prepareMessages("APOverdue", null, null,
                users, null, null);

        assertNotNull(preparators);
        assertEquals(1, preparators.length);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void testCC() {
        Collection<EmailUser> users = new ArrayList<EmailUser>();

        users.add(activeWithEmailFrench);
        users.add(disabledWithEmailFrench);
        users.add(activeWithoutEmailFrench);
        users.add(disabledWithoutEmailFrench);

        when(chunker.getPreparators(eq("APOverdue"), eq((Map<String, String>) null),
                eq(Locale.FRENCH), eq((Collection) null), any(Collection.class),
                eq((Collection) null), eq((Collection) null)))
            .thenReturn(Collections.singleton(preparator));

        MimeMessagePreparator[] preparators = emailService.prepareMessages("APOverdue", null,
                users, null, null, null);

        assertNotNull(preparators);
        assertEquals(1, preparators.length);
    }

    /**
     * JAVADOC.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void testLocales() {
        Collection<EmailUser> users = new ArrayList<EmailUser>();

        users.add(activeWithEmailFrench);
        users.add(english);
        when(chunker.getPreparators(eq("APOverdue"), eq((Map<String, String>) null),
                eq(Locale.FRENCH), any(Collection.class), eq((Collection) null),
                eq((Collection) null), eq((Collection) null)))
            .thenReturn(Collections.singleton(preparator));
        when(chunker.getPreparators(eq("APOverdue"), eq((Map<String, String>) null),
                eq(Locale.ENGLISH), any(Collection.class), eq((Collection) null),
                eq((Collection) null), eq((Collection) null)))
            .thenReturn(Collections.singleton(mock(MimeMessagePreparator.class)));

        MimeMessagePreparator[] preparators = emailService.prepareMessages("APOverdue", users,
                null, null, null, null);

        assertNotNull(preparators);
        assertEquals("Should have 2 due to differnet locales", 2, preparators.length);
    }

    /**
     * JAVADOC.
     */
    @Test
    public void testNoEmails() {
        MimeMessagePreparator[] preparators = null;

        preparators = emailService.prepareMessages("APOverdue", null, null, null, null, null);

        assertNotNull(preparators);
        assertEquals("Should have no preparators", 0, preparators.length);
    }

    /**
     * JAVADOC.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTemplateRequired() {
        emailService.prepareMessages(null, null, null, null, null, null);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void testTo() {
        Collection<EmailUser> users = new ArrayList<EmailUser>();

        users.add(activeWithEmailFrench);
        users.add(disabledWithEmailFrench);
        users.add(activeWithoutEmailFrench);
        users.add(disabledWithoutEmailFrench);
        when(chunker.getPreparators(eq("APOverdue"), eq((Map<String, String>) null),
                eq(Locale.FRENCH), any(Collection.class), eq((Collection) null),
                eq((Collection) null), eq((Collection) null)))
            .thenReturn(Collections.singleton(preparator));

        MimeMessagePreparator[] preparators = emailService.prepareMessages("APOverdue", users,
                null, null, null, null);

        assertNotNull(preparators);
        assertEquals(1, preparators.length);
    }

    private class LocalUser
        implements EmailUser {
        private Locale locale;
        private String email;
        private String username;
        private boolean active;

        public LocalUser(boolean active, String email, Locale locale, String username) {
            super();
            this.active = active;
            this.email = email;
            this.locale = locale;
            this.username = username;
        }

        @Override
        public boolean isActive() {
            return active;
        }

        @Override
        public String getEmail() {
            return email;
        }

        @Override
        public Locale getLocale() {
            return locale;
        }

        @Override
        public String getUsername() {
            return username;
        }
    }
}
