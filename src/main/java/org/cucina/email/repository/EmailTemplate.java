package org.cucina.email.repository;

import java.math.BigInteger;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import org.cucina.email.model.Template;

/**
 * JAVADOC for Class Level
 *
 * @author vlevine
 */
@Document
public class EmailTemplate extends Template {
	@Id
	private BigInteger id;

	private Date lastModified;

	@Version
	private Integer version;

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
