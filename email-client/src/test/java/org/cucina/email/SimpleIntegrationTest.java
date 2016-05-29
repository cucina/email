package org.cucina.email;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.cucina.email.client.api.ApiClient;
import org.cucina.email.client.api.DefaultApi;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
@WebIntegrationTest(randomPort = true)
public class SimpleIntegrationTest {
	@Value("${local.server.port}")
	private int port;

	private DefaultApi api;

	@Before
	public void setup() {
		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + port + "/email/v1");
		api = new DefaultApi(client);
	}

	@Test
	public void test() throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("templates/Test.ftl").getFile());
		api.createTemplate("test", file, null);
		
		System.err.println(api.getTemplate("test", null));
	}

}
