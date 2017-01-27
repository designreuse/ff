package org.ff.rest.dashboard.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ff.jpa.domain.Impression.EntityType;
import org.ff.jpa.domain.Item;
import org.ff.jpa.domain.Item.ItemMetaTag;
import org.ff.jpa.domain.ProjectItem;
import org.ff.jpa.domain.Tender.TenderStatus;
import org.ff.jpa.domain.User.UserRegistrationType;
import org.ff.jpa.repository.ImpressionRepository;
import org.ff.jpa.repository.ItemRepository;
import org.ff.jpa.repository.ProjectItemRepository;
import org.ff.jpa.repository.ProjectRepository;
import org.ff.jpa.repository.TenderRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.currency.service.CurrencyService;
import org.ff.rest.dashboard.resource.ChartResource;
import org.ff.rest.dashboard.resource.DashboardResource;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DashboardService {

	public enum DashboardChartType { USERS, VISITS, TENDERS };
	public enum DashboardChartPeriod { DAILY, MONTHLY };

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ImpressionRepository impressionRepository;

	@Autowired
	private TenderRepository tenderRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ProjectItemRepository projectItemRepository;

	@Autowired
	private CurrencyService currencyService;

	public DashboardResource getData() {
		DashboardResource resource = new DashboardResource();

		// users
		resource.setUsersRegisteredInternal(userRepository.count(UserRegistrationType.INTERNAL));
		resource.setUsersRegisteredExternal(userRepository.count(UserRegistrationType.EXTERNAL));
		resource.setUsersRegistered(resource.getUsersRegisteredInternal() + resource.getUsersRegisteredExternal());

		if (resource.getUsersRegistered() == 0) {
			resource.setUsersRegisteredInternalPercentage("0%");
			resource.setUsersRegisteredExternalPercentage("0%");
		} else {
			double usersRegisteredInternalPercentage = (new Double(resource.getUsersRegisteredInternal()) / new Double(resource.getUsersRegistered())) * 100;
			double usersRegisteredExternalPercentage = (new Double(resource.getUsersRegisteredExternal()) / new Double(resource.getUsersRegistered())) * 100;

			resource.setUsersRegisteredInternalPercentage(new BigDecimal(usersRegisteredInternalPercentage).setScale(2, RoundingMode.HALF_UP).toString() + "%");
			resource.setUsersRegisteredExternalPercentage(new BigDecimal(usersRegisteredExternalPercentage).setScale(2, RoundingMode.HALF_UP).toString() + "%");
		}

		// visits
		resource.setVisitsTotal(impressionRepository.count(EntityType.USER));
		resource.setVisitsUnique(impressionRepository.countDistinct(EntityType.USER));

		double visitsUniquePercentage = (new Double(resource.getVisitsUnique()) / new Double(resource.getVisitsTotal())) * 100;
		if (visitsUniquePercentage > 0) {
			resource.setVisitsUniquePercentage(new BigDecimal(visitsUniquePercentage).setScale(2, RoundingMode.HALF_UP).toString() + "%");
		}

		// tenders
		resource.setTendersActive(tenderRepository.count(TenderStatus.ACTIVE));
		resource.setTendersInactive(tenderRepository.count(TenderStatus.INACTIVE));
		resource.setTenders(resource.getTendersActive() + resource.getTendersInactive());

		if (resource.getTenders() == 0) {
			resource.setTendersActivePercentage("0%");
			resource.setTendersInactivePercentage("0%");
		} else {
			double tendersActivePercentage = (new Double(resource.getTendersActive()) / new Double(resource.getTenders())) * 100;
			double tendersInactivePercentage = (new Double(resource.getTendersInactive()) / new Double(resource.getTenders())) * 100;

			resource.setTendersActivePercentage(new BigDecimal(tendersActivePercentage).setScale(2, RoundingMode.HALF_UP).toString() + "%");
			resource.setTendersInactivePercentage(new BigDecimal(tendersInactivePercentage).setScale(2, RoundingMode.HALF_UP).toString() + "%");
		}

		// projects
		resource.setProjects(projectRepository.count());

		List<Item> items = itemRepository.findByMetaTag(ItemMetaTag.COMPANY_INVESTMENT_AMOUNT);
		if (items.isEmpty()) {
			log.warn("Item with metatag COMPANY_INVESTMENT_AMOUNT not found");
			resource.setProjectsValueTotal(0d);
			resource.setProjectsValueAvg(0d);
		} else if (items.size() > 1) {
			log.warn("Multiple items with metatag COMPANY_INVESTMENT_AMOUNT found");
			resource.setProjectsValueTotal(0d);
			resource.setProjectsValueAvg(0d);
		} else {
			double total = 0d;
			for (ProjectItem projectItem : projectItemRepository.findByItem(items.get(0))) {
				String value = projectItem.getValue();
				if (StringUtils.isNotBlank(value)) {
					total = total + Double.parseDouble(value);
				}
			}
			resource.setProjectsValueTotal(total);

			double avg = (new Double(total) / new Double(resource.getProjects()));
			resource.setProjectsValueAvg(avg);
		}

		// currency
		resource.setCurrency(currencyService.findAll().get(0).getCode());

		return resource;
	}

	public ChartResource getChartData(DashboardChartPeriod period, DashboardChartType type) {
		ChartResource resource = new ChartResource();

		resource.setSerie1(new LinkedList<LinkedList<Object>>());
		if (type == DashboardChartType.VISITS) {
			resource.setSerie2(new LinkedList<LinkedList<Object>>());
		}

		if (period == DashboardChartPeriod.DAILY) {
			DateTime date = new DateTime().toDateTime(DateTimeZone.UTC).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
			for (int i = 0; i<7; i++) {
				DateTime from = date;
				DateTime to = date.withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999);

				LinkedList<Object> data = new LinkedList<>();
				data.add(from.toDate());
				if (type == DashboardChartType.USERS) {
					data.add(userRepository.count(from, to));
				} else if (type == DashboardChartType.VISITS) {
					data.add(impressionRepository.count(EntityType.USER, from, to));
				} else if (type == DashboardChartType.TENDERS) {
					data.add(tenderRepository.count(from, to));
				}
				resource.getSerie1().add(data);

				if (type == DashboardChartType.VISITS) {
					LinkedList<Object> data2 = new LinkedList<>();
					data2.add(from.toDate());
					data2.add(impressionRepository.countDistinct(EntityType.USER, from, to));
					resource.getSerie2().add(data2);
				}

				date = date.minusDays(1);
			}
		} else if (period == DashboardChartPeriod.MONTHLY) {
			DateTime date = new DateTime().toDateTime(DateTimeZone.UTC).withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
			for (int i = 0; i<6; i++) {
				DateTime from = date;
				DateTime to = date.plusMonths(1).minusDays(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999);

				log.info("{} - {}", from, to);

				LinkedList<Object> data = new LinkedList<>();
				data.add(from.toDate());
				if (type == DashboardChartType.USERS) {
					data.add(userRepository.count(from, to));
				} else if (type == DashboardChartType.VISITS) {
					data.add(impressionRepository.count(EntityType.USER, from, to));
				} else if (type == DashboardChartType.TENDERS) {
					data.add(tenderRepository.count(from, to));
				}
				resource.getSerie1().add(data);

				if (type == DashboardChartType.VISITS) {
					LinkedList<Object> data2 = new LinkedList<>();
					data2.add(from.toDate());
					data2.add(impressionRepository.countDistinct(EntityType.USER, from, to));
					resource.getSerie2().add(data2);
				}

				date = date.minusMonths(1);
			}
		}

		return resource;
	}

}
