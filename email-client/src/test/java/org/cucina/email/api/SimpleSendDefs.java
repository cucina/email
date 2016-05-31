package org.cucina.email.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;

import javax.mail.internet.MimeMessage;

import com.icegreen.greenmail.util.GreenMailUtil;

import org.cucina.email.client.model.EmailDescriptor;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SimpleSendDefs extends AbstractDefs {
	private File file;

	@Given("^template$")
	public void template(String content) throws Throwable {
		file = File.createTempFile("cuke", ".ftl");
		file.deleteOnExit();
		FileWriter fw = new FileWriter(file);
		fw.write(content);
		fw.flush();
		fw.close();
		local.set(content);
		api().createTemplate("simple", file, null);
	}

	@When("^I send mail to=(.*)$")
	public void i_send_mail(String to) throws Throwable {
		EmailDescriptor descriptor = new EmailDescriptor();
		descriptor.setTo(Collections.singletonList(to));
		descriptor.setMessageKey("simple");
		descriptor.setSubject("subject");
		descriptor.from("me@home.com");

		api().sendEmail(descriptor);
	}

	@Then("^I should read the same email$")
	public void i_should_read_the_same_email() throws Throwable {
		MimeMessage[] messages = TemplateTest.greenMail.getReceivedMessages();
		assertNotNull("Messages are null", messages);
		assertTrue("Messages are empty", messages.length > 0);
		MimeMessage mm = messages[0];
		String body = GreenMailUtil.getBody(mm);
		assertTrue(((String)local.get()).endsWith(body));
	}

}
