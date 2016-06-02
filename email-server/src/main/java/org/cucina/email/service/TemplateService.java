package org.cucina.email.service;

import org.cucina.email.model.Template;

public interface TemplateService {
	/**
	 * Creates template(s) for the locale chain as in BCP 47. For a locale
	 * en-GB-oed there should be a chain of 4 templates bound to locales:
	 * 
	 * <pre>
	  null,
	  en,
	  en-GB,
	  en-GB-oed
	 * </pre>
	 * 
	 * The chain, or any part of it will be created should it be missing.
	 * 
	 * @param name
	 * @param locale
	 * @param body
	 */
	void createTemplate(String name, String locale, String body);

	/**
	 * Retrieve best template with the given name for the locale, traversing the
	 * locale chain until a stored one is found.
	 * 
	 * @param name
	 * @param locale
	 * @return template or null if not found
	 */
	Template findBest(String name, String locale);
}
