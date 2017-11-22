package org.ff.rest.dashboard.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.ff.base.properties.BaseProperties;
import org.ff.common.algorithm.AlgorithmService;
import org.ff.common.security.AppUserDetails;
import org.ff.jpa.domain.Article.ArticleStatus;
import org.ff.jpa.domain.Item;
import org.ff.jpa.domain.Item.ItemMetaTag;
import org.ff.jpa.domain.Tender;
import org.ff.jpa.domain.Tender.TenderState;
import org.ff.jpa.domain.Tender.TenderStatus;
import org.ff.jpa.domain.TenderItem;
import org.ff.jpa.domain.User;
import org.ff.jpa.repository.ArticleRepository;
import org.ff.jpa.repository.ProjectRepository;
import org.ff.jpa.repository.TenderRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.dashboard.resource.DashboardResource;
import org.ff.rest.debugging.resource.DebuggingResource;
import org.ff.rest.item.resource.ItemResource;
import org.ff.rest.item.resource.ItemResourceAssembler;
import org.ff.rest.tender.resource.TenderResource;
import org.ff.rest.tender.resource.TenderResourceAssembler;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DashboardService {

	@Autowired
	private BaseProperties baseProperties;

	@Autowired
	private TenderRepository tenderRepository;

	@Autowired
	private ItemResourceAssembler itemResourceAssembler;

	@Autowired
	private TenderResourceAssembler tenderResourceAssembler;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AlgorithmService algorithmService;

	private SimpleDateFormat monthFormat;

	@PostConstruct
	public void init() {
		monthFormat = new SimpleDateFormat(baseProperties.getMonthFormat());
	}

	public DashboardResource getData(AppUserDetails principal) {
		DashboardResource resource = new DashboardResource();

		LocalDate today = new LocalDate();
		DateTime date3MonthsAgo = new DateTime().minusMonths(3);
		DateTime date30DaysAgo = new DateTime().minusDays(30); // latest tenders

		Map<String, AtomicInteger> map1 = new LinkedHashMap<>(); // tenders count by month
		Map<String, Double> map2 = new LinkedHashMap<>(); // tenders total value by month

		for (int i = 0; i<6; i++) {
			date3MonthsAgo = date3MonthsAgo.plusMonths(1);
			map1.put(monthFormat.format(date3MonthsAgo.toDate()), new AtomicInteger(0));
			map2.put(monthFormat.format(date3MonthsAgo.toDate()), new Double(0));
		}

		AtomicInteger cntTenders = new AtomicInteger(0);
		AtomicInteger cntTendersOpen = new AtomicInteger(0);

		for (Tender tender : tenderRepository.findByStatus(TenderStatus.ACTIVE)) {
			// counter tenders
			cntTenders.incrementAndGet();

			// count open tenders
			for (TenderItem tenderItem : tender.getItems()) {
				Item item = tenderItem.getItem();

				if (item.getMetaTag() == ItemMetaTag.TENDER_START_DATE) {
					if (tenderItem.getValue() != null) {
						LocalDate startDate = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDateTime(tenderItem.getValue()).toLocalDate();
						if (startDate.isBefore(today)) {
							cntTendersOpen.incrementAndGet();
						}
					}
				}

				if (item.getMetaTag() == ItemMetaTag.TENDER_END_DATE) {
					if (tenderItem.getValue() != null) {
						LocalDate endDate = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDateTime(tenderItem.getValue()).toLocalDate();
						if (endDate.isBefore(today)) {
							cntTendersOpen.decrementAndGet();
						}
					}
				}
			}

			LocalDate tenderStartDate = getTenderStartDate(tender);
			if (tenderStartDate == null) {
				// skip tender if start date is not known
				continue;
			}

			String tenderStartDateFormatted = monthFormat.format(tenderStartDate.toDate());

			if (map2.containsKey(tenderStartDateFormatted)) {
				for (TenderItem tenderItem : tender.getItems()) {
					Item item = tenderItem.getItem();
					if (item.getMetaTag() == ItemMetaTag.TENDER_AVAILABLE_FUNDING) {
						if (StringUtils.isNotBlank(tenderItem.getValue())) {
							double value = Double.parseDouble(tenderItem.getValue());
							map2.put(tenderStartDateFormatted, map2.get(tenderStartDateFormatted) + value);
						}
					}
				}
			}

			if (map1.containsKey(tenderStartDateFormatted)) {
				map1.get(tenderStartDateFormatted).incrementAndGet();

				if (tenderStartDate.isBefore(date30DaysAgo.toLocalDate())) {
					// skip tenders that are older then 30 days
					continue;
				}

				TenderResource tenderResource = new TenderResource();
				tenderResource.setId(tender.getId());
				tenderResource.setName(tender.getName());
				tenderResource.setCreationDate(tender.getCreationDate().toDate());
				tenderResource.setItems(new ArrayList<ItemResource>());
				resource.getTenders().add(tenderResource);

				for (TenderItem tenderItem : tender.getItems()) {
					Item item = tenderItem.getItem();
					if (item.getMetaTag() == ItemMetaTag.TENDER_START_DATE || item.getMetaTag() == ItemMetaTag.TENDER_END_DATE
							|| item.getMetaTag() == ItemMetaTag.TENDER_AVAILABLE_FUNDING || item.getMetaTag() == ItemMetaTag.TENDER_MAX_AID_INTENSITY) {

						ItemResource itemResource = itemResourceAssembler.toResource(item, true);
						tenderResourceAssembler.setResourceValue(tenderResource, itemResource, tenderItem);
						tenderResource.getItems().add(itemResource);

						if (item.getMetaTag() == ItemMetaTag.TENDER_START_DATE) {
							if (tenderItem.getValue() != null) {
								LocalDate startDate = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDateTime(tenderItem.getValue()).toLocalDate();
								if (startDate.isBefore(today) || startDate.isEqual(today)) {
									tenderResource.setState(TenderState.OPEN);
								} else if (startDate.isAfter(today)) {
									tenderResource.setState(TenderState.PENDING);
								}
							}
						}

						if (item.getMetaTag() == ItemMetaTag.TENDER_END_DATE) {
							if (tenderItem.getValue() != null) {
								LocalDate endDate = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDateTime(tenderItem.getValue()).toLocalDate();
								if (endDate.isBefore(today)) {
									tenderResource.setState(TenderState.CLOSE);
								}
							}
						}
					}
				}
			}
		}

		for (Entry<String, AtomicInteger> entry : map1.entrySet()) {
			resource.getChartLabels().add(entry.getKey());
			resource.getChartDataSeria1().add(entry.getValue());
		}

		for (Entry<String, Double> entry : map2.entrySet()) {
			resource.getChartDataSeria2().add(entry.getValue());
		}

		User user = userRepository.findOne(principal.getUser().getId());

		resource.setCntTenders(cntTenders.intValue());
		resource.setCntTendersOpen(cntTendersOpen.intValue());
		resource.setCntTenders4U(algorithmService.findTenders4User(user, user.getCompany(), projectRepository.findByCompany(user.getCompany()), new DebuggingResource()).size()); // TODO: this might slow down things

		resource.setCntProjects(projectRepository.countByCompany(user.getCompany()));
		resource.setCntArticles(articleRepository.countByStatus(ArticleStatus.ACTIVE));

		return resource;
	}

	private LocalDate getTenderStartDate(Tender tender) {
		try {
			if (tender != null && tender.getItems() != null) {
				for (TenderItem tenderItem : tender.getItems()) {
					if (tenderItem.getItem().getMetaTag() == ItemMetaTag.TENDER_START_DATE) {
						return DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDateTime(tenderItem.getValue()).toLocalDate();
					}
				}
			}
		} catch (Exception e) {
			log.error("Getting tender's start date failed", e);
		}

		return null;
	}

	public List<TenderResource> getChartDetails(String period) {
		List<TenderResource> result = new LinkedList<>();

		LocalDate today = new LocalDate();

		for (Tender tender : tenderRepository.findByStatus(TenderStatus.ACTIVE)) {
			LocalDate tenderStartDate = getTenderStartDate(tender);
			if (tenderStartDate == null) {
				// skip tender if start date is not known
				continue;
			}

			if (period.equals(monthFormat.format(tenderStartDate.toDate()))) {
				TenderResource tenderResource = new TenderResource();
				tenderResource.setId(tender.getId());
				tenderResource.setName(tender.getName());
				tenderResource.setItems(new ArrayList<ItemResource>());
				result.add(tenderResource);

				for (TenderItem tenderItem : tender.getItems()) {
					Item item = tenderItem.getItem();
					if (item.getMetaTag() == ItemMetaTag.TENDER_START_DATE || item.getMetaTag() == ItemMetaTag.TENDER_END_DATE
							|| item.getMetaTag() == ItemMetaTag.TENDER_AVAILABLE_FUNDING || item.getMetaTag() == ItemMetaTag.TENDER_MAX_AID_INTENSITY) {

						ItemResource itemResource = itemResourceAssembler.toResource(item, true);
						tenderResourceAssembler.setResourceValue(tenderResource, itemResource, tenderItem);
						tenderResource.getItems().add(itemResource);

						if (item.getMetaTag() == ItemMetaTag.TENDER_START_DATE) {
							if (tenderItem.getValue() != null) {
								LocalDate startDate = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDateTime(tenderItem.getValue()).toLocalDate();
								if (startDate.isBefore(today)) {
									tenderResource.setState(TenderState.OPEN);
								} else if (startDate.isAfter(today)) {
									tenderResource.setState(TenderState.PENDING);
								}
							}
						}

						if (item.getMetaTag() == ItemMetaTag.TENDER_END_DATE) {
							if (tenderItem.getValue() != null) {
								LocalDate endDate = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDateTime(tenderItem.getValue()).toLocalDate();
								if (endDate.isBefore(today)) {
									tenderResource.setState(TenderState.CLOSE);
								}
							}
						}
					}
				}
			}
		}

		return result;
	}

}
