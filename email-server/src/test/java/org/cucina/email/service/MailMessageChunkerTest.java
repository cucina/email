package org.cucina.email.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.javamail.MimeMessagePreparator;

import org.cucina.email.service.model.EmailUser;

/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class MailMessageChunkerTest {
	private Collection<EmailUser> ccs;

	private Collection<EmailUser> tos;

	private EmailUser user1;

	private EmailUser user10;

	private EmailUser user2;

	private EmailUser user3;

	private EmailUser user4;

	private EmailUser user5;

	private EmailUser user6;

	private EmailUser user7;

	private EmailUser user8;

	private EmailUser user9;

	private MailMessageChunkerImpl mailMessageChunker = new MailMessageChunkerImpl();

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void setUp() throws Exception {
		ccs = new ArrayList<EmailUser>();
		tos = new ArrayList<EmailUser>();

		user1 = new LocalUser(true, "ajoffe@cucina.org", null, "anna");
		user2 = new LocalUser(true, "thornton@cucina.org", null, "me");
		user3 = new LocalUser(true, "vlevine@cucina.org", null, "v");
		user4 = new LocalUser(true, "hkelsey@cucina.org", null, "h");
		user5 = new LocalUser(true, "jnguyen@cucina.org", null, "anna");
		user6 = new LocalUser(true, "lefebvre@cucina.org", null, "j");
		user7 = new LocalUser(true, "ewillson@cucina.org", null, "ed");
		user8 = new LocalUser(true, "nbradley@cucina.org", null, "Nitty");
		user9 = new LocalUser(true, "9@cucina.org", null, "9");
		user10 = new LocalUser(true, "10@cucina.org", null, "10");

		mailMessageChunker.setChunkSize(3);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Test
	public void test1Chunk() {
		tos.add(user1);
		tos.add(user2);
		tos.add(user3);

		MimeMessagePreparatorFactory factory = mock(MimeMessagePreparatorFactory.class);
		MimeMessagePreparator preparator1 = mock(MimeMessagePreparator.class);

		when(factory.getInstance("TemplateReject", null, Locale.getDefault(), tos, ccs, null))
				.thenReturn(preparator1);
		mailMessageChunker.setMessagePreparatorFactory(factory);

		Collection<MimeMessagePreparator> preparators = mailMessageChunker
				.getPreparators("TemplateReject", null, Locale.getDefault(), tos, ccs, null);

		assertNotNull(preparators);
		assertEquals("should be one as we have 3 to recipients with chunkSize=3", 1,
				preparators.size());
		verify(factory).getInstance("TemplateReject", null, Locale.getDefault(), tos, ccs, null);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void test2Chunks() {
		tos.add(user1);
		tos.add(user2);
		tos.add(user3);
		tos.add(user4);

		MimeMessagePreparatorFactory factory = mock(MimeMessagePreparatorFactory.class);
		MimeMessagePreparator preparator1 = mock(MimeMessagePreparator.class);

		Collection<EmailUser> three = new ArrayList<EmailUser>();

		three.add(user1);
		three.add(user2);
		three.add(user3);
		when(factory.getInstance("TemplateReject", null, Locale.getDefault(), three, null, null))
				.thenReturn(preparator1);

		Collection<EmailUser> one = new ArrayList<EmailUser>();

		one.add(user4);

		MimeMessagePreparator preparator2 = mock(MimeMessagePreparator.class);

		when(factory.getInstance("TemplateReject", null, Locale.getDefault(), one, null, null))
				.thenReturn(preparator2);
		mailMessageChunker.setMessagePreparatorFactory(factory);

		Collection<MimeMessagePreparator> preparators = mailMessageChunker
				.getPreparators("TemplateReject", null, Locale.getDefault(), tos, null, null);

		assertEquals("should be two as we have 4 to recipients with chunkSize=3", 2,
				preparators.size());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testMore() {
		tos.add(user1);
		tos.add(user2);
		tos.add(user3);
		tos.add(user4);
		tos.add(user5);
		tos.add(user6);

		ccs.add(user7);
		ccs.add(user8);
		ccs.add(user9);
		ccs.add(user10);

		MimeMessagePreparatorFactory factory = mock(MimeMessagePreparatorFactory.class);

		Collection<EmailUser> three1 = new ArrayList<EmailUser>();

		three1.add(user1);
		three1.add(user2);
		three1.add(user3);

		MimeMessagePreparator preparator1 = mock(MimeMessagePreparator.class);

		when(factory.getInstance("TemplateReject", null, Locale.getDefault(), three1, null, null))
				.thenReturn(preparator1);

		Collection<EmailUser> three2 = new ArrayList<EmailUser>();

		three2.add(user4);
		three2.add(user5);
		three2.add(user6);

		MimeMessagePreparator preparator2 = mock(MimeMessagePreparator.class);

		when(factory.getInstance("TemplateReject", null, Locale.getDefault(), three2, null, null))
				.thenReturn(preparator2);

		Collection<EmailUser> three3 = new ArrayList<EmailUser>();

		three3.add(user7);
		three3.add(user8);
		three3.add(user9);

		MimeMessagePreparator preparator3 = mock(MimeMessagePreparator.class);

		when(factory.getInstance("TemplateReject", null, Locale.getDefault(), null, three3, null))
				.thenReturn(preparator3);

		Collection<EmailUser> one = new ArrayList<EmailUser>();

		one.add(user10);

		MimeMessagePreparator preparator4 = mock(MimeMessagePreparator.class);

		when(factory.getInstance("TemplateReject", null, Locale.getDefault(), null, one, null))
				.thenReturn(preparator4);
		mailMessageChunker.setMessagePreparatorFactory(factory);

		Collection<MimeMessagePreparator> preparators = mailMessageChunker
				.getPreparators("TemplateReject", null, Locale.getDefault(), tos, ccs, null);

		assertEquals(4, preparators.size());
		verify(factory).getInstance("TemplateReject", null, Locale.getDefault(), null, one, null);
	}

	private class LocalUser implements EmailUser {
		private Locale locale;

		private String email;

		private String username;

		private boolean active;

		public LocalUser(boolean active, String email, Locale locale, String username) {
			super();
			this.active = active;
			this.email = email;
			this.locale = locale;
			this.username = username;
		}

		@Override
		public boolean isActive() {
			return active;
		}

		@Override
		public String getEmail() {
			return email;
		}

		@Override
		public Locale getLocale() {
			return locale;
		}

		@Override
		public String getUsername() {
			return username;
		}
	}
}
