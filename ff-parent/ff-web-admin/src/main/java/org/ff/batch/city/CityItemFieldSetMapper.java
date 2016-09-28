package org.ff.batch.city;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CityItemFieldSetMapper implements FieldSetMapper<CityItem> {

	@Override
	public CityItem mapFieldSet(FieldSet fieldSet) throws BindException {
		CityItem item = new CityItem();
		item.setId(fieldSet.readInt("id"));
		item.setName(fieldSet.readString("city"));
		item.setCounty(fieldSet.readString("zupanija"));
		item.setGroup(fieldSet.readString("skupina"));
		return item;
	}

}
