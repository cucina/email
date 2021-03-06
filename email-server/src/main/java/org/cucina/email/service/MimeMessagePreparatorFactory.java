package org.cucina.email.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import org.cucina.email.service.model.EmailUser;

import freemarker.template.Configuration;

/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Component
public class MimeMessagePreparatorFactory {
	private Configuration configuration;

	// Injected parameter map containing standard info for the email
	private Map<String, String> standardParams;

	/**
	 * @param configuration The configuration to set.
	 */
	@Required
	@Autowired
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	/**
	 * JAVADOC.
	 *
	 * @param standardParams JAVADOC.
	 */
	public void setStandardParams(Map<String, String> standardParams) {
		this.standardParams = standardParams;
	}

	/**
	 * JAVADOC.
	 *
	 * @return JAVADOC.
	 */
	public Map<String, String> getStandardParams() {
		return standardParams;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param templateName JAVADOC.
	 * @param params JAVADOC.
	 * @param locale JAVADOC.
	 * @param tos JAVADOC.
	 * @param ccs JAVADOC.
	 * @param bccs JAVADOC.
	 *
	 * @return JAVADOC.
	 */
	public MimeMessagePreparator getInstance(String templateName, Map<String, String> params,
			Locale locale, Collection<? extends EmailUser> tos, Collection<? extends EmailUser> ccs,
			Collection<? extends EmailUser> bccs) {
		if (params == null) {
			params = new HashMap<String, String>();
		}

		if (MapUtils.isNotEmpty(getStandardParams())) {
			params.putAll(getStandardParams());
		}

		MimeMessagePreparatorImpl preparator = new MimeMessagePreparatorImpl();

		preparator.setTemplateName(templateName);
		preparator.setConfiguration(configuration);
		preparator.setParams(params);
		preparator.setLocale(locale);
		preparator.setTo(tos);
		preparator.setCc(ccs);
		preparator.setBcc(bccs);

		return preparator;
	}
}
