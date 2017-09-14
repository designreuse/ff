package org.ff.jpa.repository;

import org.ff.jpa.domain.ConfigParam;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ConfigParamRepository extends CrudRepository<ConfigParam, Integer>, JpaSpecificationExecutor<ConfigParam> {

	ConfigParam findByName(String name);

}
