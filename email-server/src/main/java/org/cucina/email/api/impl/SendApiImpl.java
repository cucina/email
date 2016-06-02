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

@Component
public class SendApiImpl implements SendApi {
	@Autowired
	private EmailPreprocessor emailPreprocessor;

	@Override
	public Callable<ResponseEntity<Void>> sendEmail(@RequestBody EmailDescriptor emailDescriptor) {
		//TODO check that the template exists
		//emailDescriptor.getMessageKey()
		
		emailPreprocessor.sendEmail(emailDescriptor);

		return () -> new ResponseEntity<Void>(HttpStatus.ACCEPTED);
	}
}
