package org.cucina.email.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.activation.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.MimeMessagePreparator;

/**
 * Tests for {@link PreparatorEmailServiceImpl}
 *
 * @author $Author: vlevine $
 * @version $Revision: 1.5 $
 */
public class PreparatorEmailServiceImplTest {
	@InjectMocks
	private PreparatorEmailServiceImpl els;

	@Mock
	private EmailConstructor ces;

	/**
	 * JAVADOC Method Level Comments
	 */
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test for processEvent
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testProcessEvent() {
		Collection tos = new HashSet();
		Collection ccs = new HashSet();
		Collection bccs = new HashSet();
		Map<String, String> parameters = new HashMap<String, String>();
		Collection<DataSource> attachments = new HashSet<DataSource>();
		MimeMessagePreparatorImpl mm = mock(MimeMessagePreparatorImpl.class);

		when(ces.prepareMessages("template", tos, ccs, bccs, parameters))
				.thenReturn(Collections.singleton(mm));

		MimeMessagePreparator[] result = els.prepareMessages("subject", "from", tos, ccs, bccs,
				"template", parameters, attachments);

		verify(ces).prepareMessages("template", tos, ccs, bccs, parameters);
		verify(mm).setFrom("from");
		verify(mm).setSubject("subject");
		verify(mm).setAttachments(attachments);

		assertEquals(1, result.length);

	}
}
