package org.cucina.email.api.impl;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import org.cucina.email.api.SendApi;
import org.cucina.email.model.EmailDescriptor;
import org.cucina.email.service.EmailPreprocessor;

import reactor.bus.Event;
import reactor.bus.EventBus;

@Component
public class SendApiImpl implements SendApi {
	@Autowired
	private EventBus eventBus;

	@Override
	public Callable<ResponseEntity<Void>> sendEmail(@RequestBody EmailDescriptor emailDescriptor) {
		eventBus.notify(EmailPreprocessor.ADDRESS, Event.wrap(emailDescriptor));

		return () -> new ResponseEntity<Void>(HttpStatus.ACCEPTED);
	}
}
