package org.ff.rest.dashboard.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.ff.base.properties.BaseProperties;
import org.ff.jpa.domain.Item;
import org.ff.jpa.domain.Item.ItemMetaTag;
import org.ff.jpa.domain.Tender;
import org.ff.jpa.domain.Tender.TenderStatus;
import org.ff.jpa.domain.TenderItem;
import org.ff.jpa.repository.ProjectRepository;
import org.ff.jpa.repository.TenderRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.dashboard.resource.DashboardResource;
import org.ff.rest.item.resource.ItemResource;
import org.ff.rest.item.resource.ItemResourceAssembler;
import org.ff.rest.tender.resource.TenderResource;
import org.ff.rest.tender.resource.TenderResourceAssembler;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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
	private UserRepository userRepository;

	private SimpleDateFormat monthFormat;

	@PostConstruct
	public void init() {
		monthFormat = new SimpleDateFormat(baseProperties.getMonthFormat());
	}

	public DashboardResource getData(UserDetails principal) {
		DashboardResource resource = new DashboardResource();

		DateTime date = new DateTime();
		DateTime date2 = date.minusDays(30);

		Map<String, AtomicInteger> map = new TreeMap<>();
		for (int i = 0; i<12; i++) {
			map.put(monthFormat.format(date.toDate()), new AtomicInteger(0));
			date = date.minusMonths(1);
		}

		AtomicInteger cntTenders = new AtomicInteger(0);
		for (Tender tender : tenderRepository.findAll()) {
			if (tender.getStatus() == TenderStatus.INACTIVE) {
				// skip inactive tenders
				continue;
			}

			cntTenders.incrementAndGet();

			String s = monthFormat.format(tender.getCreationDate().toDate());
			if (map.containsKey(s)) {
				map.get(s).incrementAndGet();

				if (tender.getCreationDate().isBefore(date2)) {
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
					if (item.getMetaTag() == ItemMetaTag.TENDER_START_DATE || item.getMetaTag() == ItemMetaTag.TENDER_END_DATE || item.getMetaTag() == ItemMetaTag.TENDER_AVAILABLE_FUNDING) {
						ItemResource itemResource = itemResourceAssembler.toResource(item, true);
						tenderResourceAssembler.setResourceValue(tenderResource, itemResource, tenderItem);
						tenderResource.getItems().add(itemResource);
					}
				}
			}
		}

		for (Entry<String, AtomicInteger> entry : map.entrySet()) {
			resource.getChartLabels().add(entry.getKey());
			resource.getChartData().add(entry.getValue());
		}

		resource.setCntTenders(cntTenders.intValue());

		resource.setCntProjects(projectRepository.countByCompany(userRepository.findByEmail(principal.getUsername()).getCompany()));

		return resource;
	}

}
