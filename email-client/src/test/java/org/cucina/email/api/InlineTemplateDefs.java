package org.cucina.email.api;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;

import org.cucina.email.client.model.Template;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class InlineTemplateDefs extends AbstractDefs {
	private File file;

	@Given("^template=(.*)$")
	public void template_abcde_fgh(String content) throws Throwable {
		file = File.createTempFile("cuke", ".ftl");
		file.deleteOnExit();
		FileWriter fw = new FileWriter(file);
		fw.write(content);
		fw.flush();
		fw.close();
		local.set(content);
	}

	@When("^I create template with name=(.*) and locale=(.*)$")
	public void i_create_template_with_name_and_locale(String name, String locale)
			throws Throwable {
		api().createTemplate(name, file, locale);
	}

	@Then("^I should get the same template with name=(.*) and locale=(.*)$")
	public void i_should_get_the_same_template_with_name_and_locale(String name, String locale)
			throws Throwable {
		Template t = api().getTemplate(name, locale);
		assertEquals(local.get(), t.getBody());
	}

	
}
