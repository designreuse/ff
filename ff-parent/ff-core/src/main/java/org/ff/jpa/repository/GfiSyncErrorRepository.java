package org.ff.jpa.repository;

import org.ff.jpa.domain.GfiSyncError;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface GfiSyncErrorRepository extends CrudRepository<GfiSyncError, Integer>, JpaSpecificationExecutor<GfiSyncError> {

}
