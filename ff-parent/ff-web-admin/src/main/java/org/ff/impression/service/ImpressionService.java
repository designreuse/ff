package org.ff.impression.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.ff.jpa.domain.Impression.EntityType;
import org.ff.jpa.repository.ArticleRepository;
import org.ff.jpa.repository.ImpressionRepository;
import org.ff.jpa.repository.TenderRepository;
import org.ff.properties.BaseProperties;
import org.ff.resource.impression.ImpressionStatisticsResource;
import org.ff.service.BaseService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class ImpressionService extends BaseService {

	public enum StatisticsPeriod { LAST_7_DAYS, LAST_6_MONTHS }

	private SimpleDateFormat monthFormat, dateFormat;

	@Autowired
	private BaseProperties properties;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ImpressionRepository impressionRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private TenderRepository tenderRepository;

	@PostConstruct
	public void init() {
		monthFormat = new SimpleDateFormat(properties.getMonthFormat());
		dateFormat = new SimpleDateFormat(properties.getDateFormat());
	}

	public ImpressionStatisticsResource getImpressionStatistics(EntityType entityType, Integer entityId, StatisticsPeriod statisticsPeriod, Locale locale) {
		ImpressionStatisticsResource result = new ImpressionStatisticsResource();

		if (entityType == EntityType.ARTICLE) {
			result.setName(articleRepository.findOne(entityId).getName());
		} else if (entityType == EntityType.TENDER) {
			result.setName(tenderRepository.findOne(entityId).getName());
		}

		List<String> lstSeries = Arrays.asList(
				messageSource.getMessage("statistics.totalImpressions", null, locale),
				messageSource.getMessage("statistics.uniqueImpressions", null, locale));
		List<String> lstLabels = new ArrayList<>();
		List<Number> lstDataTotal = new ArrayList<>();
		List<Number> lstDataUnique = new ArrayList<>();

		DateTime now = new DateTime();

		if (statisticsPeriod == StatisticsPeriod.LAST_7_DAYS) {
			// get data for whole period
			DateTime start = now.minusDays(6).withTime(0, 0, 0, 0);
			DateTime end = now.withTime(23, 59, 59, 999);

			result.setTotal(impressionRepository.count(entityType, entityId, start, end));
			result.setUnique(impressionRepository.countDistinct(entityType, entityId, start, end));

			// get data by each time unit (day) of period
			for (int i=6; i>=0; i--) {
				DateTime dateTime = now.minusDays(i).withTime(0, 0, 0, 0);
				lstLabels.add(dateFormat.format(dateTime.toDate()));

				start = dateTime;
				end = dateTime.withTime(23, 59, 59, 999);

				lstDataTotal.add(impressionRepository.count(entityType, entityId, start, end));
				lstDataUnique.add(impressionRepository.countDistinct(entityType, entityId, start, end));
			}
		} else if (statisticsPeriod == StatisticsPeriod.LAST_6_MONTHS) {
			// get data for whole period
			DateTime start = (now.minusMonths(5)).withDayOfMonth(1).withTime(0, 0, 0, 0);
			DateTime end = now.plusMonths(1).withDayOfMonth(1).minusDays(1).withTime(23, 59, 59, 999);

			result.setTotal(impressionRepository.count(entityType, entityId, start, end));
			result.setUnique(impressionRepository.countDistinct(entityType, entityId, start, end));

			// get data by each time unit (month) of period
			for (int i=5; i>=0; i--) {
				DateTime dateTime = (now.minusMonths(i)).withDayOfMonth(1).withTime(0, 0, 0, 0);
				lstLabels.add(monthFormat.format(dateTime.toDate()));

				start = dateTime;
				end = start.plusMonths(1).minusDays(1).withTime(23, 59, 59, 999);

				lstDataTotal.add(impressionRepository.count(entityType, entityId, start, end));
				lstDataUnique.add(impressionRepository.countDistinct(entityType, entityId, start, end));
			}
		}

		result.setLabels(lstLabels);
		result.setSeries(lstSeries);
		result.setData(Arrays.asList(lstDataTotal, lstDataUnique));

		return result;
	}

}
