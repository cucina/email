package org.cucina.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import org.cucina.email.repository.RepositoryTemplateLoader;

import freemarker.cache.TemplateLoader;

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
}
