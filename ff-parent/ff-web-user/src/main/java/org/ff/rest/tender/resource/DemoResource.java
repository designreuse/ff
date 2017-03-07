package org.ff.rest.tender.resource;

import java.util.List;

import org.ff.rest.company.resource.CompanyResource;
import org.ff.rest.project.resource.ProjectResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class DemoResource {

	@JsonProperty("company")
	private CompanyResource company;

	@JsonProperty("projects")
	private List<ProjectResource> projects;

}
