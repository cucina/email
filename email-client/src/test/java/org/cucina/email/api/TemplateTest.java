package org.cucina.email.api;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import org.cucina.email.Application;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * @author $author$
 */
@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty", "html:build/cucumber" })
public class TemplateTest {
	public static final String REMOTE_URL = "feature.test.url";

	public static final int PORT = 8090;

	@BeforeClass
	public static void startup() {
		String remoteUrl = System.getProperty(REMOTE_URL);
		if (isBlank(remoteUrl)) {
			Application.main(
					new String[] { "--server.port=" + PORT, "--spring.profiles.active=test" });
		}
	}
}