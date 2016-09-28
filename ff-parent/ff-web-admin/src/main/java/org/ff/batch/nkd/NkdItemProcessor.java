package org.ff.batch.nkd;

import org.ff.jpa.domain.Nkd;
import org.ff.jpa.repository.NkdRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class NkdItemProcessor implements ItemProcessor<NkdItem, Nkd> {

	@Autowired
	private NkdRepository nkdRepository;

	@Override
	public Nkd process(NkdItem item) throws Exception {
		if (nkdRepository.findOne(item.getId()) == null) {
			Nkd entity = new Nkd();
			entity.setId(item.getId());
			entity.setSector(item.getSector());
			entity.setSectorName(item.getSectorName());
			entity.setArea(item.getArea());
			entity.setActivity(item.getActivity());
			entity.setActivityName(item.getActivityName());
			return entity;
		} else {
			return null;
		}
	}

}
