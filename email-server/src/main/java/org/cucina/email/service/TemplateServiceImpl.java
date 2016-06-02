package org.cucina.email.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.cucina.email.model.Template;
import org.cucina.email.repository.EmailTemplate;
import org.cucina.email.repository.TemplateRepository;

@Service
public class TemplateServiceImpl implements TemplateService {
	private static final Logger LOG = LoggerFactory.getLogger(TemplateServiceImpl.class);

	@Autowired
	private TemplateRepository templateRepository;

	@Override
	public void createTemplate(String name, String locale, String body) {
		List<Locale> localeList = buildLocaleList(locale);
		Locale l0 = localeList.get(0);
		EmailTemplate et = new EmailTemplate();
		et.setBody(body);
		et.setLastModified(new Date());
		et.setName(buildName(name, l0));
		et.setLocale(l0 == null ? null : l0.toString());
		templateRepository.save(et);

		if (localeList.size() > 1) {
			for (Locale l : localeList.subList(1, localeList.size())) {
				String localName = buildName(name, l);
				if (null == templateRepository.findByName(localName)) {
					EmailTemplate etl = new EmailTemplate();
					etl.setBody(body);
					etl.setLastModified(new Date());
					etl.setName(localName);
					etl.setLocale(l == null ? null : l.toString());
					templateRepository.save(etl);
				} else {
					break;
				}
			}
		}
	}

	@Override
	public Template findBest(String name, String locale) {
		List<Locale> localeList = buildLocaleList(locale);

		for (Locale l : localeList) {
			Template t = templateRepository.findByName(buildName(name, l));
			if (t != null) {
				return t;
			}
		}

		return null;
	}

	private List<Locale> buildLocaleList(String locale) {
		List<Locale> localeList = new ArrayList<>();

		if (StringUtils.isNotEmpty(locale)) {
			try {
				Locale l = LocaleUtils.toLocale(locale);
				localeList.addAll(LocaleUtils.localeLookupList(l));
			} catch (Exception e) {
				// if an invalid locale passed in send email in default one
				LOG.warn("Failed to parse locale from string '{}'", locale);
			}
		}

		localeList.add(null);

		return localeList;
	}

	private String buildName(String name, Locale locale) {
		return name + ((locale == null) ? "" : ("_" + locale.toString()));
	}
}
