package org.ff.jpa.repository;

import org.ff.jpa.domain.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, Integer>, JpaSpecificationExecutor<Image> {

	Page<Image> findAll(Pageable pageable);

}
