package org.cucina.email.repository;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import freemarker.cache.TemplateLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of Freemarker TemplateLoader
 *
 * @author vlevine
 */
@Component
public class RepositoryTemplateLoader implements TemplateLoader {
	private static final Logger LOG = LoggerFactory.getLogger(RepositoryTemplateLoader.class);

	@Autowired
	private TemplateRepository templateRepository;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param templateSource JAVADOC.
	 *
	 * @return JAVADOC.
	 */
	@Override
	public long getLastModified(Object templateSource) {
		return ((EmailTemplate) templateSource).getLastModified().getTime();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param templateSource JAVADOC.
	 * @param encoding JAVADOC.
	 *
	 * @return JAVADOC.
	 *
	 * @throws IOException JAVADOC.
	 */
	@Override
	public Reader getReader(Object templateSource, String encoding) throws IOException {
		return new StringReader(((EmailTemplate) templateSource).getBody());
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param templateSource JAVADOC.
	 *
	 * @throws IOException JAVADOC.
	 */
	@Override
	public void closeTemplateSource(Object templateSource) throws IOException {
		// noop
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param name JAVADOC.
	 *
	 * @return JAVADOC.
	 *
	 * @throws IOException JAVADOC.
	 */
	@Override
	public Object findTemplateSource(String name) throws IOException {
		LOG.debug("Asked for template '{}'", name);

		return templateRepository.findByName(name);
	}
}
