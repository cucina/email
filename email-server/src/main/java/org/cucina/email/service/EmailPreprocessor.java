package org.cucina.email.service;

import org.cucina.email.model.EmailDescriptor;

public interface EmailPreprocessor {

	void sendEmail(EmailDescriptor emailDescriptor);

}