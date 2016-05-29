package org.cucina.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import org.cucina.email.model.EmailDescriptor;
import org.cucina.email.service.EmailPreprocessor;

/**
 * Handles JMS text message with body as a JSON document representing EmailDto.
 * Following are the properties:
 * <ul>
 * <li>'messageKey' - name of the template to use</li>
 * <li>'to' - CSV list of addresses</li>
 * <li>'cc' - CSV list of addresses</li>
 * <li>'bcc' - CSV list of addresses</li>
 * <li>'locale' - optional String representation of a locale in which the
 * message is preferred to be</li>
 * </ul>
 *
 * @author vlevine $
 */
@Component
public class JmsEmailHandler {
	@Autowired
	private EmailPreprocessor emailPreprocessor;

	/**
	 * endpoint method
	 *
	 * @param message JAVADOC.
	 */
	@JmsListener(destination = "cucina.email", containerFactory = "myJmsListenerContainerFactory")
	public void processMessage(EmailDescriptor dto) {
		emailPreprocessor.sendEmail(dto);
	}
}
