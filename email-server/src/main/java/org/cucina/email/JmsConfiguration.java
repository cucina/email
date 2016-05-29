package org.cucina.email;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.cucina.email.model.EmailDescriptor;

@Configuration
@EnableJms
public class JmsConfiguration {
	private static final Logger LOG = LoggerFactory.getLogger(JmsConfiguration.class);
    @Autowired
    private Environment environment;
    
    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Bean
    public DefaultJmsListenerContainerFactory myJmsListenerContainerFactory(
        ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

        factory.setDestinationResolver(destinationResolver());
        factory.setConcurrency("5");
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter(objectMapper));

        return factory;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    private DestinationResolver destinationResolver() {
        return new DestinationResolver() {
                private DestinationResolver dynamicDestinationResolver = new DynamicDestinationResolver();

                @Override
                public Destination resolveDestinationName(Session session, String destinationName,
                    boolean pubSubDomain)
                    throws JMSException {
                    String dname = environment.getProperty("jms.destination." + destinationName,
                            destinationName);

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Resolved destination '" + destinationName + "' to '" + dname +
                            "'");
                    }

                    return dynamicDestinationResolver.resolveDestinationName(session, dname,
                        pubSubDomain);
                }
            };
    }

    private MessageConverter messageConverter(final ObjectMapper objectMapper) {
        return new MessageConverter() {
                @Override
                public Message toMessage(Object object, Session session)
                    throws JMSException, MessageConversionException {
                    // Unused yet
                    return null;
                }

                @Override
                public Object fromMessage(Message message)
                    throws JMSException, MessageConversionException {
                    String body = ((TextMessage) message).getText();

                    try {
                        return objectMapper.readValue(body, EmailDescriptor.class);
                    } catch (Exception e) {
                        LOG.error("Oops", e);
                        throw new MessageConversionException("Failed to convert", e);
                    }
                }
            };
    }
}
