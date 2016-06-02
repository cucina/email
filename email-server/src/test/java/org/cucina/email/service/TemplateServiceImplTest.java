package org.cucina.email.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.cucina.email.repository.EmailTemplate;
import org.cucina.email.repository.TemplateRepository;

public class TemplateServiceImplTest {
	@InjectMocks
	private TemplateServiceImpl service = new TemplateServiceImpl();

	@Mock
	private TemplateRepository templateRepository;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCreateNullTemplate() {
		service.createTemplate("name", "null", "body");
		ArgumentCaptor<EmailTemplate> are = ArgumentCaptor.forClass(EmailTemplate.class);
		verify(templateRepository, times(1)).save(are.capture());
		List<EmailTemplate> results = are.getAllValues();
		assertEquals("name", results.get(0).getName());
	}

	@Test
	public void testCreateAllTemplates() {
		service.createTemplate("name", "en_GB", "body");
		ArgumentCaptor<EmailTemplate> are = ArgumentCaptor.forClass(EmailTemplate.class);
		verify(templateRepository, times(3)).save(are.capture());
		List<EmailTemplate> results = are.getAllValues();
		assertEquals("name_en_GB", results.get(0).getName());
		assertEquals("name_en", results.get(1).getName());
		assertEquals("name", results.get(2).getName());
	}

	@Test
	public void testCreateSomeTemplates() {
		when(templateRepository.findByName("name")).thenReturn(new EmailTemplate());
		service.createTemplate("name", "en_GB", "body");
		ArgumentCaptor<EmailTemplate> are = ArgumentCaptor.forClass(EmailTemplate.class);
		verify(templateRepository, times(2)).save(are.capture());
		List<EmailTemplate> results = are.getAllValues();
		assertEquals("name_en_GB", results.get(0).getName());
		assertEquals("name_en", results.get(1).getName());
	}

	@Test
	public void testCreateOneTemplate() {
		when(templateRepository.findByName("name_en")).thenReturn(new EmailTemplate());
		service.createTemplate("name", "en_GB", "body");
		ArgumentCaptor<EmailTemplate> are = ArgumentCaptor.forClass(EmailTemplate.class);
		verify(templateRepository, times(1)).save(are.capture());
		List<EmailTemplate> results = are.getAllValues();
		assertEquals("name_en_GB", results.get(0).getName());
	}

	@Test
	public void testFindBest() {
		EmailTemplate t0 = new EmailTemplate();
		t0.setName("name");
		when(templateRepository.findByName("name")).thenReturn(t0);
		assertEquals(t0, service.findBest("name", "en_GB_oed"));

		EmailTemplate t1 = new EmailTemplate();
		t1.setName("name_en");
		when(templateRepository.findByName("name_en")).thenReturn(t1);
		assertEquals(t1, service.findBest("name", "en_GB_oed"));

		EmailTemplate t2 = new EmailTemplate();
		t2.setName("name_en_GB");
		when(templateRepository.findByName("name_en_GB")).thenReturn(t2);
		assertEquals(t2, service.findBest("name", "en_GB_oed"));

		EmailTemplate t3 = new EmailTemplate();
		t3.setName("name_en_GB_oed");
		when(templateRepository.findByName("name_en_GB_oed")).thenReturn(t3);
		assertEquals(t3, service.findBest("name", "en_GB_oed"));
	}

}
