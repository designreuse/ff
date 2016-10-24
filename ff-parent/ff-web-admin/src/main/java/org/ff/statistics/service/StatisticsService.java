package org.ff.statistics.service;

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
import org.ff.jpa.domain.City;
import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.CompanyItem;
import org.ff.jpa.domain.County;
import org.ff.jpa.domain.Investment;
import org.ff.jpa.domain.Item;
import org.ff.jpa.domain.Item.ItemMetaTag;
import org.ff.jpa.domain.ItemOption;
import org.ff.jpa.domain.Nkd;
import org.ff.jpa.repository.CityRepository;
import org.ff.jpa.repository.CompanyItemRepository;
import org.ff.jpa.repository.CompanyRepository;
import org.ff.jpa.repository.CountyRepository;
import org.ff.jpa.repository.InvestmentRepository;
import org.ff.jpa.repository.ItemRepository;
import org.ff.jpa.repository.NkdRepository;
import org.ff.statistics.resource.StatisticsResource;
import org.ff.statistics.resource.StatisticsResource.StatisticsType;
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
	private CountyRepository countyRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private NkdRepository nkdRepository;

	@Autowired
	private InvestmentRepository investmentRepository;

	@Autowired
	private CompanyRepository companyRepository;

	/**
	 * Get companies by counties.
	 * @return
	 */
	public ResponseEntity<?> getCompaniesByCounties() {
		StatisticsResource result = new StatisticsResource();
		result.setType(StatisticsType.COMPANIES_BY_COUNTIES);
		result.setTimestamp(new Date());

		List<County> lstCounty = Lists.newArrayList(countyRepository.findAll());
		Collections.sort(lstCounty, new Comparator<County>() {
			@Override
			public int compare(County o1, County o2) {
				return collator.compare(o1.getName(), o2.getName());
			}
		});

		Map<County, AtomicInteger> counters = new LinkedHashMap<>();
		for (County county : lstCounty) {
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
				City city = cityRepository.findOne(Integer.parseInt(companyItem.getValue()));
				County county = city.getCounty();
				counters.put(county, new AtomicInteger(counters.get(county).incrementAndGet()));
			}
		}

		for (Entry<County, AtomicInteger> entry : counters.entrySet()) {
			result.getLabels().add(StringUtils.abbreviate(entry.getKey().getName(), abbreviateMaxWidth));
			result.getData().add(entry.getValue());
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * Get companies by investments.
	 * @return
	 */
	public ResponseEntity<?> getCompaniesByInvestments() {
		StatisticsResource result = new StatisticsResource();

		Map<Investment, AtomicInteger> counters = new LinkedHashMap<>();
		for (Investment investment : investmentRepository.findAll()) {
			counters.put(investment, new AtomicInteger(0));
		}

		for (Entry<Investment, AtomicInteger> entry : counters.entrySet()) {
			result.getLabels().add(StringUtils.abbreviate(entry.getKey().getName(), abbreviateMaxWidth));
			result.getData().add(entry.getValue());
		}

		for (Company company : companyRepository.findAll()) {
			for (Investment investment : company.getInvestments()) {
				counters.put(investment, new AtomicInteger(counters.get(investment).incrementAndGet()));
			}
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
			itemOptions.put(itemOption.getId(),itemOption);
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
	 * Get companies by sectors.
	 * @return
	 */
	public ResponseEntity<?> getCompaniesBySectors() {
		StatisticsResource result = new StatisticsResource();
		result.setType(StatisticsType.COMPANIES_BY_SECTORS);
		result.setTimestamp(new Date());

		List<String> sectors = nkdRepository.getSectors();
		Collections.sort(sectors, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return collator.compare(o1, o2);
			}
		});

		Map<String, AtomicInteger> counters = new LinkedHashMap<>();
		for (String sector : sectors) {
			counters.put(sector, new AtomicInteger(0));
		}

		List<Item> items = itemRepository.findByMetaTag(ItemMetaTag.COMPANY_SECTOR);
		if (items.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else if (items.size() > 1) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		for (CompanyItem companyItem : companyItemRepository.findByItem(items.get(0))) {
			if (companyItem.getValue() != null) {
				Nkd nkd = nkdRepository.findOne(Integer.parseInt(companyItem.getValue()));
				String sector = nkd.getSectorName();
				counters.put(sector, new AtomicInteger(counters.get(sector).incrementAndGet()));
			}
		}

		for (Entry<String, AtomicInteger> entry : counters.entrySet()) {
			result.getLabels().add(StringUtils.abbreviate(entry.getKey(), abbreviateMaxWidth));
			result.getData().add(entry.getValue());
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
