package org.cucina.email;

import static reactor.bus.selector.Selectors.$;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import org.cucina.email.service.AsyncEventSender;

import freemarker.cache.TemplateLoader;
import reactor.Environment;
import reactor.bus.EventBus;

// TODO consider dedicated TaskExecutor for running async emailPreprocessor from SendApiImpl

@Configuration
@EnableAsync
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
	EventBus createEventBus(Environment env, AsyncEventSender asyncEventSender) {
		EventBus eb = EventBus.create(env, Environment.THREAD_POOL);
		eb.on($(AsyncEventSender.class), asyncEventSender::send);

		return eb;
	}
}
