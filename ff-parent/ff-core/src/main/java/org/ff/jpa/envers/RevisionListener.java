package org.ff.jpa.envers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class RevisionListener implements org.hibernate.envers.RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		Revision revision = (Revision) revisionEntity;

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null) {
			revision.setCreatedBy(authentication.getName());
		}
	}

}
