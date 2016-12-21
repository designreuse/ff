package org.ff;

import java.io.FileReader;

import javax.annotation.PostConstruct;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.ff.base.service.BaseService;
import org.ff.jpa.domain.Subdivision1;
import org.ff.jpa.domain.Subdivision2;
import org.ff.jpa.repository.Subdivision1Repository;
import org.ff.jpa.repository.Subdivision2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.com.bytecode.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InitData extends BaseService {

	@Autowired
	private Subdivision1Repository subdivision1Repository;

	@Autowired
	private Subdivision2Repository subdivision2Repository;

	@PostConstruct
	public void init() {
		//		importSubdivision1();
		//		importSubdivision2();
	}

	@Transactional
	public void importSubdivision1() {
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader("/Users/dblazevic/Downloads/subdivisions1.csv"));
			for (String[] row : reader.readAll()) {
				Subdivision1 entity = new Subdivision1();
				entity.setName(row[0]);
				entity.setDevelopmentIndex(row[1]);
				subdivision1Repository.save(entity);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	@Transactional
	public void importSubdivision2() {
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader("/Users/dblazevic/Downloads/subdivisions2.csv"));
			for (String[] row : reader.readAll()) {
				Subdivision2 entity = new Subdivision2();
				entity.setName(row[0]);
				entity.setSubdivision1(subdivision1Repository.findByName(row[1]));
				entity.setDevelopmentIndex(row[2]);
				subdivision2Repository.save(entity);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

}
