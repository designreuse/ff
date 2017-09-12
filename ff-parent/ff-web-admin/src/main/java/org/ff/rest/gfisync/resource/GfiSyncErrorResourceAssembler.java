package org.ff.rest.gfisync.resource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.ff.base.properties.BaseProperties;
import org.ff.jpa.domain.BusinessRelationshipManager;
import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.GfiSyncError;
import org.ff.jpa.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GfiSyncErrorResourceAssembler {

	@Autowired
	private BaseProperties baseProperties;

	private DateFormat dateTimeFormat;

	@PostConstruct
	public void init() {
		dateTimeFormat = new SimpleDateFormat(baseProperties.getDateTimeFormat());
	}

	public GfiSyncErrorResource toResource(GfiSyncError entity, boolean light) {
		GfiSyncErrorResource resource = new GfiSyncErrorResource();
		resource.setId(entity.getId());
		resource.setUser(getUser(entity));
		if (!light) {
			resource.setCompanyData(entity.getCompanyData());
			resource.setError(entity.getError());
		}
		return resource;
	}

	public List<GfiSyncErrorResource> toResources(Iterable<GfiSyncError> entities, boolean light) {
		List<GfiSyncErrorResource> resources = new ArrayList<>();
		for (GfiSyncError entity : entities) {
			resources.add(toResource(entity, light));
		}
		return resources;
	}

	private String getUser(GfiSyncError entity) {
		User user = entity.getUser();
		Company company = user.getCompany();
		BusinessRelationshipManager brm = user.getBusinessRelationshipManager();
		BusinessRelationshipManager brmSub = user.getBusinessRelationshipManagerSubstitute();

		StringBuffer sb = new StringBuffer();
		sb.append(user.getId()).append(",");
		sb.append(user.getFirstName()).append(" ").append(user.getLastName()).append(",");
		sb.append(StringUtils.isNotBlank(company.getName()) ? company.getName() : "").append(",");
		sb.append(StringUtils.isNotBlank(company.getCode()) ? company.getCode() : "").append(",");
		sb.append(StringUtils.isNotBlank(user.getEmail()) ? user.getEmail() : "").append(",");
		sb.append(StringUtils.isNotBlank(user.getEmail2()) ? user.getEmail2() : "").append(",");
		if (brm != null) {
			sb.append(brm.getFirstName()).append(" ").append(brm.getLastName()).append(",");
		} else {
			sb.append("").append(",");
		}
		if (brmSub != null) {
			sb.append(brmSub.getFirstName()).append(" ").append(brmSub.getLastName()).append(",");
		} else {
			sb.append("").append(",");
		}
		if (user.getLastLoginDate() != null) {
			sb.append(dateTimeFormat.format(user.getLastLoginDate().toDate()));
		} else {
			sb.append("");
		}

		return sb.toString();
	}

}
