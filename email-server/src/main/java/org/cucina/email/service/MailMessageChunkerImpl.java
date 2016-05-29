package org.cucina.email.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.activation.DataSource;

import org.apache.commons.collections.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles chunking of mail recipients to avoid issues with mail server
 * limitation on number of addressees.
 *
 */
@Component
public class MailMessageChunkerImpl implements MailMessageChunker {
	private static final Logger LOG = LoggerFactory.getLogger(MailMessageChunkerImpl.class);

	@Autowired
	private MimeMessagePreparatorFactory messagePreparatorFactory;

	private int chunkSize = 100;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param chunkSize JAVADOC.
	 */
	public void setChunkSize(int chunkSize) {
		this.chunkSize = chunkSize;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param messagePreparatorFactory JAVADOC.
	 */
	public void setMessagePreparatorFactory(MimeMessagePreparatorFactory messagePreparatorFactory) {
		this.messagePreparatorFactory = messagePreparatorFactory;
	}

	/**
	 * Returns MimeMessagePreparator instances for the different locales and
	 * possibly chunks the email into separate ones
	 *
	 * @param descriptor encapsulates email.
	 * @param locale current locale.
	 * @param tos to recipients.
	 * @param ccs cc recipients.
	 * @param bccs bcc recipients.
	 * @param csEmail direct tos (not User objects).
	 *
	 * @return a set of MimeMessagePreparator objects ready to be sent.
	 */
	public Set<MimeMessagePreparator> getPreparators(String templateName,
			Map<String, String> params, Locale locale, Collection<? extends EmailUser> tos,
			Collection<? extends EmailUser> ccs, Collection<? extends EmailUser> bccs,
			Collection<DataSource> attachments) {
		Set<MimeMessagePreparator> preparators = new HashSet<MimeMessagePreparator>();
		int totalRecipients = getTotal(locale, tos, ccs, bccs);

		if (totalRecipients <= chunkSize) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Total recipients is less than the size at which recipients "
						+ "must be separated out");
			}

			preparators.add(messagePreparatorFactory.getInstance(templateName, params, locale, tos,
					ccs, bccs, attachments));
		} else {
			if (LOG.isInfoEnabled()) {
				LOG.info("Total recipients is [" + totalRecipients
						+ "] which exceeds the chunking size [" + chunkSize
						+ "]. Splitting emails to ensure no SMTP errors");
			}

			preparators.addAll(
					chunkPreparators(templateName, params, locale, tos, ccs, bccs, attachments));
		}

		return preparators;
	}

	/**
	 * Counts total number of recipients
	 *
	 * @param locale JAVADOC.
	 * @param tos JAVADOC.
	 * @param ccs JAVADOC.
	 * @param bccs JAVADOC.
	 * @param csEmail JAVADOC.
	 *
	 * @return count.
	 */
	protected int getTotal(Locale locale, Collection<?> tos, Collection<?> ccs,
			Collection<?> bccs) {
		int totalRecipients = 0;

		int toSize = getSize(tos);
		int ccSize = getSize(ccs);
		int bccSize = getSize(bccs);

		totalRecipients += toSize;
		totalRecipients += ccSize;
		totalRecipients += bccSize;

		return totalRecipients;
	}

	private int getSize(Collection<?> coll) {
		if (coll == null) {
			return 0;
		}

		return coll.size();
	}

	private <T extends EmailUser> List<List<? extends EmailUser>> chunkObjects(
			Collection<T> objColl, int chunkSize) {
		if (CollectionUtils.isEmpty(objColl)) {
			return Collections.emptyList();
		}

		List<T> objList = new ArrayList<T>(objColl);

		if (objColl.size() < chunkSize) {
			List<List<? extends EmailUser>> ret = new ArrayList<List<? extends EmailUser>>();

			ret.add(objList);

			return ret;
		}

		// ok chunking
		int noOfChunks = countChunks(objColl.size(), chunkSize);

		// convert to List so we can subList it

		// calculate the end index (which is just the size)
		int objListEndIndex = objColl.size();

		// return value
		List<List<? extends EmailUser>> ret = new ArrayList<List<? extends EmailUser>>();

		// best illustrated by example, say noOfChunks = 5, chunkSize = 60
		for (int i = 0; i < noOfChunks; i++) {
			// so 0 for first iteration, 60 for second, etc, etc
			int startPoint = chunkSize * i;

			if (startPoint > objListEndIndex) {
				break;
			}

			// so ((0+1)*60 )- 1 for second, ie. 59
			int endPoint = ((i + 1) * chunkSize);

			// if endPoint is greater than the actual number of objects we will
			// get
			// an ArrayIndexOutOfBoundsException from sublist, so we have to
			// manage it
			if (endPoint > objListEndIndex) {
				ret.add(new ArrayList<T>(objList.subList(startPoint, objListEndIndex)));

				break;
			}

			ret.add(new ArrayList<T>(objList.subList(startPoint, endPoint)));
		}

		return ret;
	}

	private Set<MimeMessagePreparator> chunkPreparators(String templateName,
			Map<String, String> params, Locale locale, Collection<? extends EmailUser> tos,
			Collection<? extends EmailUser> ccs, Collection<? extends EmailUser> bccs,
			Collection<DataSource> attachments) {
		Set<MimeMessagePreparator> ret = new HashSet<MimeMessagePreparator>();

		// convert all 4 to lists
		List<List<? extends EmailUser>> chunkedTos = chunkObjects(tos, chunkSize);

		if (LOG.isDebugEnabled()) {
			LOG.debug("Split To recipients into [" + chunkedTos.size() + "] blocks");
		}

		List<List<? extends EmailUser>> chunkedCcs = chunkObjects(ccs, chunkSize);

		if (LOG.isDebugEnabled()) {
			LOG.debug("Split Cc recipients into [" + chunkedCcs.size() + "] blocks");
		}

		List<List<? extends EmailUser>> chunkedBccs = chunkObjects(bccs, chunkSize);

		if (LOG.isDebugEnabled()) {
			LOG.debug("Split Bcc recipients into [" + chunkedBccs.size() + "] blocks");
		}

		for (List<? extends EmailUser> chunk : chunkedTos) {
			ret.add(messagePreparatorFactory.getInstance(templateName, params, locale, chunk, null,
					null, attachments));
		}

		for (List<? extends EmailUser> chunk : chunkedCcs) {
			ret.add(messagePreparatorFactory.getInstance(templateName, params, locale, null, chunk,
					null, attachments));
		}

		for (List<? extends EmailUser> chunk : chunkedBccs) {
			ret.add(messagePreparatorFactory.getInstance(templateName, params, locale, null, null,
					chunk, attachments));
		}

		return ret;
	}

	private <T> int countChunks(int total, int chunkSize) {
		int intNoOfChunks = (total / chunkSize) + (((total % chunkSize) > 0) ? 1 : 0);

		return intNoOfChunks;
	}
}
