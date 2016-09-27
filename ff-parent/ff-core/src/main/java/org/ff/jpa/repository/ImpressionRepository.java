package org.ff.jpa.repository;

import org.ff.jpa.domain.Impression;
import org.ff.jpa.domain.Impression.EntityType;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ImpressionRepository extends CrudRepository<Impression, Integer>, JpaSpecificationExecutor<Impression> {

	@Query("select count(*) from Impression i where i.entityType = :entityType and i.entityId = :entityId and (i.creationDate between :start and :end)")
	Long count(
			@Param("entityType") EntityType entityType,
			@Param("entityId") Integer entityId,
			@Param("start") DateTime start,
			@Param("end") DateTime end);

	@Query("select count(distinct created_by) from Impression i where i.entityType = :entityType and i.entityId = :entityId and (i.creationDate between :start and :end)")
	Long countDistinct(
			@Param("entityType") EntityType entityType,
			@Param("entityId") Integer entityId,
			@Param("start") DateTime start,
			@Param("end") DateTime end);

}
