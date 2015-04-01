package org.cucina.email.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.activation.DataSource;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import org.cucina.email.service.EmailConstructor;
import org.cucina.email.service.EmailServiceImpl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link EmailServiceImpl}
 *
 * @author $Author: vlevine $
 * @version $Revision: 1.5 $
 */
public class EmailServiceImplTest {
    private EmailServiceImpl els;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setUp() {
        els = new EmailServiceImpl();
    }

    /**
     * Test for processEvent
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void testProcessEvent() {
        JavaMailSender jms = mock(JavaMailSender.class);

        jms.send(any(MimeMessagePreparator[].class));
        els.setJavaMailSender(jms);

        Collection tos = new HashSet();
        Collection ccs = new HashSet();
        Collection bccs = new HashSet();
        Map<String, Object> parameters = new HashMap<String, Object>();
        Collection<DataSource> attachments = new HashSet<DataSource>();

        EmailConstructor ces = mock(EmailConstructor.class);

        when(ces.prepareMessages("template", tos, ccs, bccs, parameters, attachments)).thenReturn(new MimeMessagePreparator[] {
                new MimeMessagePreparatorImpl() {
                    public void prepare(MimeMessage mimeMessage)
                        throws MessagingException {
                        throw new UnsupportedOperationException("shouldn't be called");
                    }

                    public String toString() {
                        return "Stub";
                    }
                }
            });
        els.setEmailConstructor(ces);
        els.sendMessages("subject", "from", tos, ccs, bccs, "template", parameters, attachments);

        verify(ces).prepareMessages("template", tos, ccs, bccs, parameters, attachments);
    }

    /**
     * Test for when {@link JavaMailSender} throws exception
     */
    @Test
    public void testProcessEventError() {
        final String exceptionMessage = "whatever";

        JavaMailSender jms = mock(JavaMailSender.class);

        doThrow(new MailSendException(exceptionMessage)).when(jms)
            .send(any(MimeMessagePreparator[].class));

        els.setJavaMailSender(jms);

        EmailConstructor ces = mock(EmailConstructor.class);

        when(ces.prepareMessages(null, null, null, null, null, null)).thenReturn(new MimeMessagePreparator[] {
                new MimeMessagePreparatorImpl() {
                    public void prepare(MimeMessage mimeMessage)
                        throws MessagingException {
                        throw new UnsupportedOperationException("shouldn't be called");
                    }

                    public String toString() {
                        return "Stub";
                    }
                }
            });
        els.setEmailConstructor(ces);

        try {
            els.sendMessages(null, null, null, null, null, null, null, null);
        } catch (Exception e) {
            assertEquals(exceptionMessage, e.getMessage());

            return;
        }

        fail("should have transformed mailsendexception to emailexception...");
    }
}
