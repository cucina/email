package org.cucina.email;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
  */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class EmailJmsClient {
    @Autowired
    private JmsTemplate jmsTemplate;
    private ObjectMapper om = new ObjectMapper();

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void sendEmail()
        throws Exception {
        EmailDto dto = new EmailDto();

        dto.setTo("cucina@opencucina.org");
        dto.setFrom("cucina@opencucina.org");
        dto.setSubject("Hello there");
        //        dto.setMessageKey("NoSubject");
        dto.setMessageKey("new");

        dto.setLocale("xxxxx");
        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("ObjectID", "Waste of time");
        parameters.put("URL", "http://localhost:8080");
        dto.setParameters(parameters);

        final String text = om.writeValueAsString(dto);

        System.err.println(text);

        jmsTemplate.send(new MessageCreator() {
                public Message createMessage(Session session)
                    throws JMSException {
                    Message mess = session.createTextMessage(text);

                    return mess;
                }
            });
    }
}
