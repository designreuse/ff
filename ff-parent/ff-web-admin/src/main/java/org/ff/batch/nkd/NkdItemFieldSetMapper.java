package org.ff.batch.nkd;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class NkdItemFieldSetMapper implements FieldSetMapper<NkdItem> {

	@Override
	public NkdItem mapFieldSet(FieldSet fieldSet) throws BindException {
		NkdItem item = new NkdItem();
		item.setId(fieldSet.readInt("id"));
		item.setSector(fieldSet.readString("sektor"));
		item.setSectorName(fieldSet.readString("naziv_sektora"));
		item.setArea(fieldSet.readString("podrucje"));
		item.setActivity(fieldSet.readString("djelatnost"));
		item.setActivityName(fieldSet.readString("naziv_djelatnosti"));
		return item;
	}

}
