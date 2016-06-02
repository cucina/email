package org.cucina.email.api.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import org.cucina.email.api.TemplateApi;
import org.cucina.email.model.Template;
import org.cucina.email.repository.EmailTemplate;
import org.cucina.email.repository.TemplateRepository;

@Component
public class TemplateApiImpl implements TemplateApi {
	private static final Logger LOG = LoggerFactory.getLogger(TemplateApiImpl.class);

	@Autowired
	private TemplateRepository templateRepository;

	@Override
	public Callable<ResponseEntity<Void>> createTemplate(String name, MultipartFile file,
			String locale) {
		if (!file.isEmpty()) {
			EmailTemplate et = new EmailTemplate();

			try {
				byte[] bytes = file.getBytes();
				String body = new String(bytes, StandardCharsets.UTF_8);
				et.setBody(body);
			} catch (IOException e) {
				LOG.error("Oops", e);
				// to cause 500 in DefaultHandlerExceptionResolver
				throw new RuntimeException(e);
			}

			if (templateRepository.findByName(name) == null) {
				// create default template as a catchall if a localised one is
				// not found
			}

			et.setName(buildName(name, locale));
			et.setLastModified(new Date());
			templateRepository.save(et);

			return () -> new ResponseEntity<Void>(HttpStatus.CREATED);
		}

		// Should cause 400 in DefaultHandlerExceptionResolver
		throw new HttpMessageNotReadableException("No file provided");
	}

	@Override
	public Callable<ResponseEntity<Template>> getTemplate(String name, String locale) {
		Template et = templateRepository.findByName(buildName(name, locale));
		// TODO handle not found

		return () -> new ResponseEntity<>(et, HttpStatus.OK);
	}

	private String buildName(String name, String locale) {
		return name + ((locale == null) ? "" : ("_" + locale.toString()));
	}
}
