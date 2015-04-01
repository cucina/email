package org.cucina.email.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import javax.activation.DataSource;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;

import freemarker.template.Configuration;

import org.cucina.email.service.EmailUser;
import org.cucina.email.service.MimeMessagePreparatorImpl;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
public class MimeMessagePreparatorImplTest {
    private static final String USER2_EMAIL = "vlevine@cucina.org";
    private static final String USER1_EMAIL = "ajoffe@cucina.org";
    private static final String SUFFIX = ".ftl";
    private static final String MIME_TYPE = "text/html";
    private static final String LOCATION = "classpath:templates/";
    private static final String FROM = "admin@cucina.org";

    /**
    * JAVADOC.
    *
    * @throws Exception JAVADOC.
    */
    @Before
    public void setUp()
        throws Exception {
    }

    /**
     * JAVADOC.
     */
    @Test
    public void testAll()
        throws Exception {
        MimeMessagePreparatorImpl preparator = getPreparatorInstance("Test",
                new HashMap<String, Object>(), Locale.FRANCE, MIME_TYPE, LOCATION, FROM, SUFFIX);

        // create users
        Collection<EmailUser> users = new ArrayList<EmailUser>();
        EmailUser user = constructUser(USER1_EMAIL);

        users.add(user);

        EmailUser user2 = constructUser(USER2_EMAIL);

        users.add(user2);
        preparator.setTo(users);
        preparator.setCc(users);
        preparator.setBcc(users);
        preparator.setParams(new HashMap<String, Object>());

        MimeMessage message = mock(MimeMessage.class);

        message.setFrom(new InternetAddress(FROM));
        message.addRecipient(RecipientType.TO, new InternetAddress(USER1_EMAIL));
        message.addRecipient(RecipientType.TO, new InternetAddress(USER2_EMAIL));
        when((message.getRecipients(RecipientType.TO)))
            .thenReturn(new Address[] {
                new InternetAddress(USER1_EMAIL), new InternetAddress(USER2_EMAIL)
            });

        message.addRecipient(RecipientType.CC, new InternetAddress(USER1_EMAIL));
        message.addRecipient(RecipientType.CC, new InternetAddress(USER2_EMAIL));
        when((message.getRecipients(RecipientType.CC)))
            .thenReturn(new Address[] {
                new InternetAddress(USER1_EMAIL), new InternetAddress(USER2_EMAIL)
            });

        message.addRecipient(RecipientType.BCC, new InternetAddress(USER1_EMAIL));
        message.addRecipient(RecipientType.BCC, new InternetAddress(USER2_EMAIL));
        when((message.getRecipients(RecipientType.BCC)))
            .thenReturn(new Address[] {
                new InternetAddress(USER1_EMAIL), new InternetAddress(USER2_EMAIL)
            });
        message.setContent(any(String.class), eq("text/html;charset=UTF-8"));
        message.setSubject(any(String.class));
        message.saveChanges();
        preparator.prepare(message);
    }

    // doesn't find template
    /**
     * JAVADOC.
     */
    @Test
    public void testCantFindTemplate()
        throws Exception {
        String suffix = ".vx";

        MimeMessagePreparatorImpl preparator = new MimeMessagePreparatorImpl();

        preparator.setConfiguration(getConfiguration(LOCATION));
        preparator.setFrom(FROM);
        preparator.setSuffix(suffix);

        preparator.setLocale(Locale.FRANCE);
        preparator.setTemplateName("Test");

        // create users
        Collection<EmailUser> users = new ArrayList<EmailUser>();
        EmailUser user = constructUser(USER1_EMAIL);

        users.add(user);

        EmailUser user2 = constructUser(USER2_EMAIL);

        users.add(user2);
        preparator.setTo(users);

        MimeMessage message = mock(MimeMessage.class);

        try {
            preparator.prepare(message);
            fail("Didn't catch expected exception");
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    /**
     * JAVADOC.
     */
    @Test
    public void testNoFrom()
        throws Exception {
        MimeMessagePreparatorImpl preparator = new MimeMessagePreparatorImpl();

        preparator.setConfiguration(getConfiguration(LOCATION));

        preparator.setLocale(Locale.FRANCE);
        preparator.setTemplateName("Test");
        preparator.setSuffix(SUFFIX);

        // create users
        Collection<EmailUser> users = new ArrayList<EmailUser>();
        EmailUser user = constructUser(USER1_EMAIL);

        users.add(user);

        EmailUser user2 = constructUser(USER2_EMAIL);

        users.add(user2);
        preparator.setTo(users);

        MimeMessage message = mock(MimeMessage.class);

        try {
            preparator.prepare(message);
            fail("Didn't catch expected exception");
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    // no locale
    /**
     * JAVADOC.
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testNoLocale()
        throws Exception {
        MimeMessagePreparatorImpl preparator = getPreparatorInstance("Test",
                new HashMap<String, Object>(), null, MIME_TYPE, LOCATION, FROM, SUFFIX);

        // create users
        Collection<EmailUser> users = new ArrayList<EmailUser>();
        EmailUser user = constructUser(USER1_EMAIL);

        users.add(user);

        EmailUser user2 = constructUser(USER2_EMAIL);

        users.add(user2);
        preparator.setTo(users);

        preparator.setParams(new HashMap<String, Object>());

        MimeMessage message = mock(MimeMessage.class);

        message.setFrom(new InternetAddress(FROM));
        message.addRecipient(RecipientType.TO, new InternetAddress(USER1_EMAIL));
        message.addRecipient(RecipientType.TO, new InternetAddress(USER2_EMAIL));
        when((message.getRecipients(RecipientType.TO)))
            .thenReturn(new Address[] {
                new InternetAddress(USER1_EMAIL), new InternetAddress(USER2_EMAIL)
            });
        when((message.getRecipients(RecipientType.CC))).thenReturn(new Address[] {  });
        when((message.getRecipients(RecipientType.BCC))).thenReturn(new Address[] {  });
        message.setContent(any(String.class), eq("text/html;charset=UTF-8"));
        message.setSubject(any(String.class));
        message.saveChanges();
        preparator.prepare(message);
    }

    // no email descriptor
    /**
     * JAVADOC.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoTemplateName()
        throws Exception {
        MimeMessagePreparatorImpl preparator = getPreparatorInstance(null, null, Locale.FRANCE,
                MIME_TYPE, LOCATION, FROM, SUFFIX);

        // create users
        Collection<EmailUser> users = new ArrayList<EmailUser>();
        EmailUser user = constructUser(USER1_EMAIL);

        users.add(user);

        EmailUser user2 = constructUser(USER2_EMAIL);

        users.add(user2);
        preparator.setTo(users);

        MimeMessage message = mock(MimeMessage.class);

        preparator.prepare(message);
    }

    // to user but no email
    /**
     * JAVADOC.
     */
    @Test
    public void testNoToEmailAddress()
        throws Exception {
        MimeMessagePreparatorImpl preparator = getPreparatorInstance("Test",
                new HashMap<String, Object>(), Locale.FRANCE, MIME_TYPE, LOCATION, FROM, SUFFIX);

        // create users
        Collection<EmailUser> users = new ArrayList<EmailUser>();
        EmailUser user = constructUser(null);

        users.add(user);

        preparator.setTo(users);

        MimeMessage message = mock(MimeMessage.class);

        try {
            preparator.prepare(message);
            fail("Didn't catch expected exception");
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    // no email descriptor
    /**
     * JAVADOC.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoUsers()
        throws Exception {
        MimeMessagePreparatorImpl preparator = getPreparatorInstance("Test",
                new HashMap<String, Object>(), Locale.FRANCE, MIME_TYPE, LOCATION, FROM, SUFFIX);

        MimeMessage message = mock(MimeMessage.class);

        preparator.prepare(message);
    }

    // subject/body stuff - how to test this?
    /**
     * JAVADOC.
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testResolveCorrectMessageEnglish()
        throws Exception {
        MimeMessagePreparatorImpl preparator = getPreparatorInstance("Test",
                new HashMap<String, Object>(), Locale.ENGLISH, MIME_TYPE, LOCATION, FROM, SUFFIX);

        // create users
        Collection<EmailUser> users = new ArrayList<EmailUser>();
        EmailUser user = constructUser(USER1_EMAIL);

        users.add(user);

        EmailUser user2 = constructUser(USER2_EMAIL);

        users.add(user2);
        preparator.setTo(users);

        preparator.setParams(new HashMap<String, Object>());

        MimeMessage message = mock(MimeMessage.class);

        message.setFrom(new InternetAddress(FROM));
        message.addRecipient(RecipientType.TO, new InternetAddress(USER1_EMAIL));
        message.addRecipient(RecipientType.TO, new InternetAddress(USER2_EMAIL));
        when((message.getRecipients(RecipientType.TO)))
            .thenReturn(new Address[] {
                new InternetAddress(USER1_EMAIL), new InternetAddress(USER2_EMAIL)
            });
        when((message.getRecipients(RecipientType.CC))).thenReturn(new Address[] {  });
        when((message.getRecipients(RecipientType.BCC))).thenReturn(new Address[] {  });
        message.setContent(any(String.class), eq("text/html;charset=UTF-8"));
        message.setSubject(any(String.class));
        message.saveChanges();

        preparator.prepare(message);
    }

    // internationalisation
    /**
     * JAVADOC.
     */
    @Test
    public void testResolveCorrectMessageFrench()
        throws Exception {
        MimeMessagePreparatorImpl preparator = getPreparatorInstance("Test",
                new HashMap<String, Object>(), Locale.FRANCE, MIME_TYPE, LOCATION, FROM, SUFFIX);

        // create users
        Collection<EmailUser> users = new ArrayList<EmailUser>();
        EmailUser user = constructUser(USER1_EMAIL);

        users.add(user);

        EmailUser user2 = constructUser(USER2_EMAIL);

        users.add(user2);
        preparator.setTo(users);

        preparator.setParams(new HashMap<String, Object>());

        MimeMessage message = mock(MimeMessage.class);

        message.setFrom(new InternetAddress(FROM));
        message.addRecipient(RecipientType.TO, new InternetAddress(USER1_EMAIL));
        message.addRecipient(RecipientType.TO, new InternetAddress(USER2_EMAIL));
        when((message.getRecipients(RecipientType.TO)))
            .thenReturn(new Address[] {
                new InternetAddress(USER1_EMAIL), new InternetAddress(USER2_EMAIL)
            });
        when((message.getRecipients(RecipientType.CC))).thenReturn(new Address[] {  });
        when((message.getRecipients(RecipientType.BCC))).thenReturn(new Address[] {  });
        message.setContent(any(String.class), eq("text/html;charset=UTF-8"));
        message.setSubject(any(String.class));
        message.saveChanges();
        preparator.prepare(message);
    }

    // internationalisation
    /**
     * JAVADOC.
     */
    @Test
    public void testResolveDefaultMessageGerman()
        throws Exception {
        MimeMessagePreparatorImpl preparator = getPreparatorInstance("Test",
                new HashMap<String, Object>(), Locale.GERMAN, MIME_TYPE, LOCATION, FROM, SUFFIX);

        // create users
        Collection<EmailUser> users = new ArrayList<EmailUser>();
        EmailUser user = constructUser(USER1_EMAIL);

        users.add(user);

        EmailUser user2 = constructUser(USER2_EMAIL);

        users.add(user2);
        preparator.setTo(users);

        preparator.setParams(new HashMap<String, Object>());

        MimeMessage message = mock(MimeMessage.class);

        message.setFrom(new InternetAddress(FROM));
        message.addRecipient(RecipientType.TO, new InternetAddress(USER1_EMAIL));
        message.addRecipient(RecipientType.TO, new InternetAddress(USER2_EMAIL));
        when((message.getRecipients(RecipientType.TO)))
            .thenReturn(new Address[] {
                new InternetAddress(USER1_EMAIL), new InternetAddress(USER2_EMAIL)
            });
        when((message.getRecipients(RecipientType.CC))).thenReturn(new Address[] {  });
        when((message.getRecipients(RecipientType.BCC))).thenReturn(new Address[] {  });
        message.setContent(any(String.class), eq("text/html;charset=UTF-8"));
        message.setSubject(any(String.class));
        message.saveChanges();

        preparator.prepare(message);
    }

    /**
     * JAVADOC.
     */
    @Test
    public void testSunnyDay()
        throws Exception {
        MimeMessagePreparatorImpl preparator = getPreparatorInstance("Test",
                new HashMap<String, Object>(), Locale.FRANCE, MIME_TYPE, LOCATION, FROM, SUFFIX);

        // create users
        Collection<EmailUser> users = new ArrayList<EmailUser>();
        EmailUser user = constructUser(USER1_EMAIL);

        users.add(user);

        EmailUser user2 = constructUser(USER2_EMAIL);

        users.add(user2);
        preparator.setTo(users);
        preparator.setParams(new HashMap<String, Object>());

        MimeMessage message = mock(MimeMessage.class);

        message.setFrom(new InternetAddress(FROM));
        message.addRecipient(RecipientType.TO, new InternetAddress(USER1_EMAIL));
        message.addRecipient(RecipientType.TO, new InternetAddress(USER2_EMAIL));
        when((message.getRecipients(RecipientType.TO)))
            .thenReturn(new Address[] {
                new InternetAddress(USER1_EMAIL), new InternetAddress(USER2_EMAIL)
            });
        when((message.getRecipients(RecipientType.CC))).thenReturn(new Address[] {  });
        when((message.getRecipients(RecipientType.BCC))).thenReturn(new Address[] {  });
        message.setContent(any(String.class), eq("text/html;charset=UTF-8"));
        message.setSubject(any(String.class));
        message.saveChanges();

        preparator.prepare(message);
    }

    /**
     * JAVADOC.
     */
    @Test
    public void testSunnyDayWithParametersAndAttachments()
        throws Exception {
        DataSource ds = new ByteArrayDataSource(new byte[] { 1 }, "text/html:charset=UTF-8");

        Collection<DataSource> datasources = new HashSet<DataSource>();

        datasources.add(ds);

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("ObjectID", new Long(12345));
        params.put("URL", "http://cucina.org");

        MimeMessagePreparatorImpl preparator = getPreparatorInstance("Test", params, Locale.FRANCE,
                MIME_TYPE, LOCATION, FROM, SUFFIX);

        preparator.setAttachments(datasources);

        // create users
        Collection<EmailUser> users = new ArrayList<EmailUser>();
        EmailUser user = constructUser(USER1_EMAIL);

        users.add(user);

        EmailUser user2 = constructUser(USER2_EMAIL);

        users.add(user2);
        preparator.setTo(users);

        MimeMessage message = mock(MimeMessage.class);

        message.setFrom(new InternetAddress(FROM));
        message.addRecipient(RecipientType.TO, new InternetAddress(USER1_EMAIL));
        message.addRecipient(RecipientType.TO, new InternetAddress(USER2_EMAIL));
        when((message.getRecipients(RecipientType.TO)))
            .thenReturn(new Address[] {
                new InternetAddress(USER1_EMAIL), new InternetAddress(USER2_EMAIL)
            });
        when((message.getRecipients(RecipientType.CC))).thenReturn(new Address[] {  });
        when((message.getRecipients(RecipientType.BCC))).thenReturn(new Address[] {  });
        message.setContent(any(MimeMultipart.class));
        message.setSubject(any(String.class));
        message.saveChanges();

        preparator.prepare(message);
    }

    // no templateLocation
    /**
     * JAVADOC.
     */
    @Test
    public void testTemplateLocationNotSet()
        throws Exception {
        MimeMessagePreparatorImpl preparator = new MimeMessagePreparatorImpl();

        // preparator.setTemplateLocation(location);
        preparator.setConfiguration(getConfiguration(LOCATION));
        preparator.setFrom(FROM);

        preparator.setLocale(Locale.FRANCE);
        preparator.setTemplateName("Test");
        preparator.setSuffix(SUFFIX);

        // create users
        Collection<EmailUser> users = new ArrayList<EmailUser>();
        EmailUser user = constructUser(USER1_EMAIL);

        users.add(user);

        EmailUser user2 = constructUser(USER2_EMAIL);

        users.add(user2);
        preparator.setTo(users);

        MimeMessage message = mock(MimeMessage.class);

        try {
            preparator.prepare(message);
            fail("Didn't catch expected exception");
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    // no velocity engine
    /**
     * JAVADOC.
     */
    @Test
    public void testVelocityEngineNotSet() {
        MimeMessagePreparatorImpl preparator = new MimeMessagePreparatorImpl();

        preparator.setSuffix(SUFFIX);

        // preparator.setVelocityEngine(getVelocityEngine(location));
        preparator.setFrom(FROM);

        preparator.setLocale(Locale.FRANCE);
        preparator.setTemplateName("Test");
        preparator.setParams(new HashMap<String, Object>());

        // create users
        Collection<EmailUser> users = new ArrayList<EmailUser>();
        EmailUser user = constructUser(USER1_EMAIL);

        users.add(user);

        EmailUser user2 = constructUser(USER2_EMAIL);

        users.add(user2);
        preparator.setTo(users);

        MimeMessage message = mock(MimeMessage.class);

        try {
            preparator.prepare(message);
            fail("Didn't catch expected exception");
        } catch (Exception e) {
            // expected
        }
    }

    /**
     * JAVADOC.
     *
     * @param location JAVADOC.
     *
     * @return JAVADOC.
     */
    private Configuration getConfiguration(String location)
        throws Exception {
        FreeMarkerConfigurationFactory factory = new FreeMarkerConfigurationFactory();

        factory.setTemplateLoaderPath(location);

        return factory.createConfiguration();
    }

    private MimeMessagePreparatorImpl getPreparatorInstance(String templateName,
        Map<String, Object> params, Locale locale, String mimeType, String templateLocation,
        String from, String suffix)
        throws Exception {
        MimeMessagePreparatorImpl preparator = new MimeMessagePreparatorImpl();

        preparator.setConfiguration(getConfiguration(templateLocation));
        preparator.setFrom(from);
        preparator.setSuffix(suffix);

        preparator.setLocale(locale);
        preparator.setTemplateName(templateName);
        preparator.setParams(params);

        return preparator;
    }

    /**
     * JAVADOC.
     *
     * @param username JAVADOC.
     * @param email JAVADOC.
     *
     * @return JAVADOC.
     */
    private EmailUser constructUser(String email) {
        return new LocalUser(true, email, Locale.ENGLISH, "me");
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
