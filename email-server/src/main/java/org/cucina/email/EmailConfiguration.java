package org.cucina.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import org.cucina.email.service.EmailPreprocessor;

import freemarker.cache.TemplateLoader;
import reactor.Environment;
import reactor.bus.EventBus;
import reactor.bus.selector.RegexSelector;

@Configuration
public class EmailConfiguration {

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Bean
	@Autowired
	public FreeMarkerConfigurer freeMarkerConfigurer(TemplateLoader templateLoader) {
		FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();

		configurer.setPreTemplateLoaders(templateLoader);

		return configurer;
	}

	@Bean
	Environment env() {
		return Environment.initializeIfEmpty().assignErrorJournal();
	}

	@Bean
	@Autowired
	EventBus createEventBus(Environment env, EmailPreprocessor emailPreprocessor) {
		EventBus eb = EventBus.create(env, Environment.THREAD_POOL);
		eb.on(RegexSelector.regexSelector(EmailPreprocessor.ADDRESS), emailPreprocessor);
		
		return eb;
	}
}
