package org.ff.jpa.repository;

import java.util.List;

import org.ff.jpa.domain.ZabaMappingsLocation;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ZabaMappingsLocationRepository extends CrudRepository<ZabaMappingsLocation, Integer>, JpaSpecificationExecutor<ZabaMappingsLocation> {

	List<ZabaMappingsLocation> findByZipCode(String zipCode);

}
