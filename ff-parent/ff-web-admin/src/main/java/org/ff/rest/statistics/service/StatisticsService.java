package org.ff.rest.statistics.service;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.ff.jpa.domain.Activity;
import org.ff.jpa.domain.CompanyItem;
import org.ff.jpa.domain.Item;
import org.ff.jpa.domain.Item.ItemMetaTag;
import org.ff.jpa.domain.ItemOption;
import org.ff.jpa.domain.Project;
import org.ff.jpa.domain.ProjectItem;
import org.ff.jpa.domain.Subdivision1;
import org.ff.jpa.domain.Subdivision2;
import org.ff.jpa.repository.ActivityRepository;
import org.ff.jpa.repository.CompanyItemRepository;
import org.ff.jpa.repository.ItemRepository;
import org.ff.jpa.repository.ProjectItemRepository;
import org.ff.jpa.repository.Subdivision1Repository;
import org.ff.jpa.repository.Subdivision2Repository;
import org.ff.rest.currency.service.CurrencyService;
import org.ff.rest.statistics.resource.StatisticsResource;
import org.ff.rest.statistics.resource.StatisticsResource.StatisticsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
public class StatisticsService {

	private final int abbreviateMaxWidth = 300;

	@Autowired
	private Collator collator;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private CompanyItemRepository companyItemRepository;

	@Autowired
	private Subdivision1Repository subdivision1Repository;

	@Autowired
	private Subdivision2Repository subdivision2Repository;

	@Autowired
	private ProjectItemRepository projectItemRepository;

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private CurrencyService currencyService;

	/**
	 * Get companies by counties.
	 * @return
	 */
	public ResponseEntity<?> getCompaniesByCounties() {
		StatisticsResource result = new StatisticsResource();
		result.setType(StatisticsType.COMPANIES_BY_COUNTIES);
		result.setTimestamp(new Date());

		List<Subdivision1> lstSubdivision1 = Lists.newArrayList(subdivision1Repository.findAll());
		Collections.sort(lstSubdivision1, new Comparator<Subdivision1>() {
			@Override
			public int compare(Subdivision1 o1, Subdivision1 o2) {
				return collator.compare(o1.getName(), o2.getName());
			}
		});

		Map<Subdivision1, AtomicInteger> counters = new LinkedHashMap<>();
		for (Subdivision1 county : lstSubdivision1) {
			counters.put(county, new AtomicInteger(0));
		}

		List<Item> items = itemRepository.findByMetaTag(ItemMetaTag.COMPANY_LOCATION);
		if (items.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else if (items.size() > 1) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		for (CompanyItem companyItem : companyItemRepository.findByItem(items.get(0))) {
			if (companyItem.getValue() != null) {
				Subdivision2 subdivision2 = subdivision2Repository.findOne(Integer.parseInt(companyItem.getValue()));
				Subdivision1 subdivision1 = subdivision2.getSubdivision1();
				counters.put(subdivision1, new AtomicInteger(counters.get(subdivision1).incrementAndGet()));
			}
		}

		for (Entry<Subdivision1, AtomicInteger> entry : counters.entrySet()) {
			result.getLabels().add(StringUtils.abbreviate(entry.getKey().getName(), abbreviateMaxWidth));
			result.getData().add(entry.getValue());
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * Get companies by revenues.
	 * @return
	 */
	public ResponseEntity<?> getCompaniesByRevenues() {
		StatisticsResource result = new StatisticsResource();
		result.setType(StatisticsType.COMPANIES_BY_REVENUES);
		result.setTimestamp(new Date());

		List<Item> items = itemRepository.findByMetaTag(ItemMetaTag.COMPANY_REVENUE);
		if (items.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else if (items.size() > 1) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		Map<Integer, ItemOption> itemOptions = new LinkedHashMap<>();
		Map<Integer, AtomicInteger> counters = new LinkedHashMap<>();
		for (ItemOption itemOption : items.get(0).getOptions()) {
			itemOptions.put(itemOption.getId(), itemOption);
			counters.put(itemOption.getId(), new AtomicInteger(0));
		}

		for (CompanyItem companyItem : companyItemRepository.findByItem(items.get(0))) {
			if (companyItem.getValue() != null) {
				counters.put(Integer.parseInt(companyItem.getValue()), new AtomicInteger(counters.get(Integer.parseInt(companyItem.getValue())).incrementAndGet()));
			}
		}

		for (Entry<Integer, AtomicInteger> entry : counters.entrySet()) {
			result.getLabels().add(StringUtils.abbreviate(itemOptions.get(entry.getKey()).getText(), abbreviateMaxWidth));
			result.getData().add(entry.getValue());
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * Get companies by size.
	 * @return
	 */
	public ResponseEntity<?> getCompaniesBySize() {
		StatisticsResource result = new StatisticsResource();
		result.setType(StatisticsType.COMPANIES_BY_SIZE);
		result.setTimestamp(new Date());

		List<Item> items = itemRepository.findByMetaTag(ItemMetaTag.COMPANY_SIZE);
		if (items.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else if (items.size() > 1) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		Map<Integer, ItemOption> itemOptions = new LinkedHashMap<>();
		Map<Integer, AtomicInteger> counters = new LinkedHashMap<>();
		for (ItemOption itemOption : items.get(0).getOptions()) {
			itemOptions.put(itemOption.getId(), itemOption);
			counters.put(itemOption.getId(), new AtomicInteger(0));
		}

		for (CompanyItem companyItem : companyItemRepository.findByItem(items.get(0))) {
			if (companyItem.getValue() != null) {
				counters.put(Integer.parseInt(companyItem.getValue()), new AtomicInteger(counters.get(Integer.parseInt(companyItem.getValue())).incrementAndGet()));
			}
		}

		for (Entry<Integer, AtomicInteger> entry : counters.entrySet()) {
			result.getLabels().add(StringUtils.abbreviate(itemOptions.get(entry.getKey()).getText(), abbreviateMaxWidth));
			result.getData().add(entry.getValue());
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	public ResponseEntity<?> getInvestmentsByCounties() {
		StatisticsResource result = new StatisticsResource();
		result.setType(StatisticsType.INVESTMENTS_BY_COUNTIES);
		result.setTimestamp(new Date());
		result.setCurrency(currencyService.findAll().get(0).getCode());

		List<Subdivision1> lstSubdivision1 = Lists.newArrayList(subdivision1Repository.findAll());
		Collections.sort(lstSubdivision1, new Comparator<Subdivision1>() {
			@Override
			public int compare(Subdivision1 o1, Subdivision1 o2) {
				return collator.compare(o1.getName(), o2.getName());
			}
		});

		Map<Subdivision1, AtomicInteger> counters = new LinkedHashMap<>();
		Map<Subdivision1, Double> amounts = new LinkedHashMap<>();
		for (Subdivision1 subdivision1 : lstSubdivision1) {
			counters.put(subdivision1, new AtomicInteger(0));
			amounts.put(subdivision1, new Double(0));
		}

		List<Item> items = itemRepository.findByMetaTag(ItemMetaTag.COMPANY_INVESTMENT_SUBDIVISION1);
		if (items.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else if (items.size() > 1) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		for (ProjectItem projectItem : projectItemRepository.findByItem(items.get(0))) {
			if (projectItem.getValue() != null) {
				Subdivision1 subdivision1 = subdivision1Repository.findOne(Integer.parseInt(projectItem.getValue()));
				counters.put(subdivision1, new AtomicInteger(counters.get(subdivision1).incrementAndGet()));
				amounts.put(subdivision1, amounts.get(subdivision1).doubleValue() + getAmount(projectItem.getProject()).doubleValue());
			}
		}

		for (Entry<Subdivision1, AtomicInteger> entry : counters.entrySet()) {
			result.getLabels().add(StringUtils.abbreviate(entry.getKey().getName(), abbreviateMaxWidth));
			result.getData().add(entry.getValue());
			result.getData2().add(amounts.get(entry.getKey()));
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	public ResponseEntity<?> getInvestmentsByActivities() {
		StatisticsResource result = new StatisticsResource();
		result.setType(StatisticsType.INVESTMENTS_BY_ACTIVITIES);
		result.setTimestamp(new Date());
		result.setCurrency(currencyService.findAll().get(0).getCode());

		List<Activity> activities = Lists.newArrayList(activityRepository.findAll());
		Collections.sort(activities, new Comparator<Activity>() {
			@Override
			public int compare(Activity o1, Activity o2) {
				return collator.compare(o1.getName(), o2.getName());
			}
		});

		Map<Activity, AtomicInteger> counters = new LinkedHashMap<>();
		Map<Activity, Double> amounts = new LinkedHashMap<>();
		for (Activity activity : activities) {
			counters.put(activity, new AtomicInteger(0));
			amounts.put(activity, new Double(0));
		}

		List<Item> items = itemRepository.findByMetaTag(ItemMetaTag.COMPANY_INVESTMENT_ACTIVITY);
		if (items.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else if (items.size() > 1) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		for (ProjectItem projectItem : projectItemRepository.findByItem(items.get(0))) {
			if (projectItem.getValue() != null) {
				Activity activity = activityRepository.findOne(Integer.parseInt(projectItem.getValue()));
				counters.put(activity, new AtomicInteger(counters.get(activity).incrementAndGet()));
				amounts.put(activity, amounts.get(activity).doubleValue() + getAmount(projectItem.getProject()).doubleValue());
			}
		}

		for (Entry<Activity, AtomicInteger> entry : counters.entrySet()) {
			result.getLabels().add(StringUtils.abbreviate(entry.getKey().getName(), abbreviateMaxWidth));
			result.getData().add(entry.getValue());
			result.getData2().add(amounts.get(entry.getKey()));
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	private Double getAmount(Project project) {
		Double result = new Double(0);
		for (ProjectItem projectItem : project.getItems()) {
			if (projectItem.getItem().getMetaTag() == ItemMetaTag.COMPANY_INVESTMENT_AMOUNT) {
				if (projectItem.getValue() != null) {
					result = result.doubleValue() + Double.parseDouble(projectItem.getValue());
				}
			}
		}
		return result;
	}

}
