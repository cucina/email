package org.cucina.email.api;

import org.junit.Test;

import org.cucina.email.client.api.ApiClient;
import org.cucina.email.client.api.DefaultApi;

/**
 * @author vlevine
 */

public abstract class AbstractDefs {
    protected ThreadLocal<Object> local = new ThreadLocal<>();
    

	
	protected DefaultApi api() {
		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + TemplateTest.PORT + "/email/v1");
		return new DefaultApi(client);
	}
	
    @Test
    public void stub() {
        // need this to fool junit not to complain about this class as not
        // having a runnable methods
    }
   
}
