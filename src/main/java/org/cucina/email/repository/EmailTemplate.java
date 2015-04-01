package org.cucina.email.repository;

import java.math.BigInteger;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
@Document
public class EmailTemplate {
    @Id
    private BigInteger id;
    private Date lastModified;
    @Version
    private Integer version;
    private String body;
    private String name;

    /**
     * JAVADOC Method Level Comments
     *
     * @param body JAVADOC.
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getBody() {
        return body;
    }

    /**
     *
     *
     * @param id .
     */
    public void setId(BigInteger id) {
        this.id = id;
    }

    /**
     *
     *
     * @return .
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param lastModified JAVADOC.
     */
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Date getLastModified() {
        return lastModified;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param name JAVADOC.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getName() {
        return name;
    }

    /**
     *
     *
     * @param version .
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     *
     *
     * @return .
     */
    public Integer getVersion() {
        return version;
    }
}
