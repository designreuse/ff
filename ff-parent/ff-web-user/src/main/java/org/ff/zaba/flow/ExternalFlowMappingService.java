package org.ff.zaba.flow;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.ff.base.properties.BaseProperties;
import org.ff.jpa.domain.ItemOption;
import org.ff.jpa.repository.ItemOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExternalFlowMappingService {

	@Autowired
	private BaseProperties baseProperties;

	@Autowired
	private ItemOptionRepository itemOptionRepository;

	private Map<String, Integer> legalTypeNumbers;

	@PostConstruct
	public void init() {
		try {
			initMappingLegalTypeNumber();

			log.debug("==================== E X T E R N A L    F L O W    M A P P I N G S ====================");
			log.debug("legalTypeNumbers: {}", legalTypeNumbers);
			log.debug("=======================================================================================");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void initMappingLegalTypeNumber() throws Exception {
		legalTypeNumbers = new HashMap<>();

		String mappingLegalTypeNumber = baseProperties.getMappingLegalTypeNumber();
		if (StringUtils.isNotBlank(mappingLegalTypeNumber)) {
			for (String str : mappingLegalTypeNumber.split("\\|")) {
				String[] array = str.split("\\-");
				legalTypeNumbers.put(array[0], Integer.parseInt(array[1]));
			}
		}
	}

	public ItemOption getLegalTypeNumberMapping(String value) {
		if (!legalTypeNumbers.containsKey(value)) {
			log.warn("Unknown external value [{}] detected for legal type number", value);
			return null;
		}
		return itemOptionRepository.findOne(legalTypeNumbers.get(value));
	}

}
