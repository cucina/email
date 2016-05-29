package org.cucina.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import org.cucina.email.repository.RepositoryTemplateLoader;

@Configuration
public class EmailConfiguration {
    
    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();

        configurer.setPreTemplateLoaders(repositoryTemplateLoader());

        return configurer;
    }

    

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Bean
    public RepositoryTemplateLoader repositoryTemplateLoader() {
        return new RepositoryTemplateLoader();
    }

   
}
