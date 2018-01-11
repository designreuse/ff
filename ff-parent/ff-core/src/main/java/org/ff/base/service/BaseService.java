package org.ff.base.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.ff.base.properties.BaseProperties;
import org.ff.common.uigrid.UiGridResource;
import org.ff.common.uigrid.UiGridSortResource;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public class BaseService {

	@PersistenceContext
	protected EntityManager entityManager;

	@Autowired
	protected AuditingHandler auditingHandler;

	@Autowired
	protected BaseProperties properties;

	protected Pageable createPageable(UiGridResource resource) {
		if (resource.getSort() != null && !resource.getSort().isEmpty()) {
			return new PageRequest(resource.getPagination().getPage(), resource.getPagination().getSize(), createSort(resource));
		} else {
			return new PageRequest(resource.getPagination().getPage(), resource.getPagination().getSize());
		}
	}

	protected Sort createSort(UiGridResource resource) {
		List<Order> orders = new ArrayList<>();
		for (UiGridSortResource uiGridSortResource : resource.getSort()) {
			orders.add(new Order((uiGridSortResource.getDirection().equals("asc")) ? Direction.ASC : Direction.DESC, uiGridSortResource.getName()));
		}
		return new Sort(orders);
	}

	protected List<Date> getDateRange(String date) {
		return Arrays.asList(
				DateTime.parse(date.substring(0, 10), DateTimeFormat.forPattern(properties.getDateFormat())).withTime(0, 0, 0, 0).toDate(),
				DateTime.parse(date.substring(13, date.length()), DateTimeFormat.forPattern(properties.getDateFormat())).withTime(23, 59, 59, 999).toDate());
	}

}
