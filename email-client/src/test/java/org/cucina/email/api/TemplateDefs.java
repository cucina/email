package org.cucina.email.api;

import java.io.File;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TemplateDefs extends AbstractDefs {
	@Given("^Template is created with name=(.*) and fileName=(.*) and locale=(.*)$")
	public void template_is_created_with_name_and_fileName_and_locale(String name, String fileName,
			String locale) throws Throwable {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("templates/Test.ftl").getFile());
		api().createTemplate(name, file, locale);
	}

	@When("^I request to getTemplate with name=(.*) and locale=(.*)$")
	public void i_request_to_getTemplate_with_name_test_and_locale_null(String name, String locale)
			throws Throwable {
		local.set(api().getTemplate(name, locale));
	}

	@Then("^I should get the created template$")
	public void i_should_get_the_created_template() throws Throwable {
		System.err.println(local.get());
	}
}
