package org.cucina.email.repository;

import org.springframework.data.repository.Repository;


/**
 * JAVADOC Interface Level
 *
 * @author vlevine
  */
public interface TemplateRepository
    extends Repository<EmailTemplate, Long> {
    /**
     *
     *
     * @param id .
     *
     * @return .
     */
    EmailTemplate findById(Long id);

    /**
     *
     *
     * @param name .
     *
     * @return .
     */
    EmailTemplate findByName(String name);

    /**
     *
     *
     * @param template .
     */
    void save(EmailTemplate template);
}
