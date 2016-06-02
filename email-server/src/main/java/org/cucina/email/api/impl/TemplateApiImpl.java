package org.cucina.email.api.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import org.cucina.email.api.TemplateApi;
import org.cucina.email.model.Template;
import org.cucina.email.service.TemplateService;

@Component
public class TemplateApiImpl implements TemplateApi {
	private static final Logger LOG = LoggerFactory.getLogger(TemplateApiImpl.class);

	@Autowired
	private TemplateService templateService;

	@Override
	public Callable<ResponseEntity<Void>> createTemplate(String name, MultipartFile file,
			String locale) {
		if (!file.isEmpty()) {

			try {
				byte[] bytes = file.getBytes();
				String body = new String(bytes, StandardCharsets.UTF_8);
				templateService.createTemplate(name, locale, body);
			} catch (IOException e) {
				LOG.error("Oops", e);
				// to cause 500 in DefaultHandlerExceptionResolver
				throw new RuntimeException(e);
			}

			return () -> new ResponseEntity<Void>(HttpStatus.CREATED);
		}

		throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "No file provided");
	}

	@Override
	public Callable<ResponseEntity<Template>> getTemplate(String name, String locale) {
		Template et = templateService.findBest(name, locale);
		if (et == null) {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
					"Failed to find template for the name '" + name + "' and locale '" + locale
							+ "'");
		}

		return () -> new ResponseEntity<>(et, HttpStatus.OK);
	}
}
