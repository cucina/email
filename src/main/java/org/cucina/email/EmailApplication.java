package org.cucina.email;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.cucina.email.repository.RepositoryTemplateLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * End point of the email handler. To configure the queue on which it listens
 * for requests use property 'jms.destination.cucina.email' in
 * application.properties or its equivalent.
 *
 * @author $Author: $
 * @version $Revision: $
 */
@SpringBootApplication
@EnableJms
public class EmailApplication {
    private static final Logger LOG = LoggerFactory.getLogger(EmailApplication.class);
    @Autowired
    private Environment environment;

    /**
     * JAVADOC Method Level Comments
     *
     * @param args
     *            JAVADOC.
     *
     * @throws Exception
     *             JAVADOC.
     */
    public static void main(String[] args)
        throws Exception {
        SpringApplication.run(EmailApplication.class, args);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();

        configurer.setPreTemplateLoaders(repositoryTemplateLoader());

        return configurer;
    }

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
    @Bean
    public RepositoryTemplateLoader repositoryTemplateLoader() {
        return new RepositoryTemplateLoader();
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
                    // Unused
                    return null;
                }

                @Override
                public Object fromMessage(Message message)
                    throws JMSException, MessageConversionException {
                    String body = ((TextMessage) message).getText();

                    try {
                        return objectMapper.readValue(body, EmailDto.class);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        LOG.error("Oops", e);
                        throw new MessageConversionException("Failed to convert", e);
                    }
                }
            };
    }
}
