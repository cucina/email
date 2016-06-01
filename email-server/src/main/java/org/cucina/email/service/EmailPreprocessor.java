package org.cucina.email.service;

import org.cucina.email.model.EmailDescriptor;

import reactor.bus.Event;
import reactor.fn.Consumer;

public interface EmailPreprocessor extends Consumer<Event<EmailDescriptor>> {
	public static final String ADDRESS = "emailPreprocessor";

	void sendEmail(EmailDescriptor emailDescriptor);

}