package org.cucina.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * End point of the email handler. To configure the queue on which it listens
 * for requests use property 'jms.destination.cucina.email' in
 * application.properties or its equivalent.
 *
 * @author $Author: $
 * @version $Revision: $
 */
@SpringBootApplication
public class EmailApplication {
    

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
}
