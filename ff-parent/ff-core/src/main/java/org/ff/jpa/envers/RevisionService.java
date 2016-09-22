package org.ff.jpa.envers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.criteria.internal.IdentifierEqAuditExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RevisionService {

	@PersistenceContext
	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<RevisionResource> getRevisions(Class<?> clazz, Integer entityId) {
		List<RevisionResource> result = new ArrayList<>();
		log.debug("Getting revisions for entity [{}] with ID [{}]", clazz.getSimpleName(), entityId);
		List<Object[]> objects = AuditReaderFactory.get(entityManager).createQuery().forRevisionsOfEntity(clazz, false, true).add(new IdentifierEqAuditExpression(entityId, true)).getResultList();
		for (Object[] object : objects) {
			RevisionResource resource = new RevisionResource();
			resource.setClazz(clazz.getName());
			resource.setEntityId(entityId);
			Revision revision = (Revision) object[1];
			resource.setId(revision.getId());
			resource.setCreationDate(revision.getCreationDate());
			resource.setCreatedBy(revision.getCreatedBy());
			resource.setType(RevisionType.valueOf(object[2].toString()));
			result.add(resource);
		}
		return result;
	}

	@Transactional(readOnly = true)
	public RevisionResource getPreviousRevision(RevisionResource revision) {
		try {
			List<RevisionResource> revisions = getRevisions(Class.forName(revision.getClazz()), revision.getEntityId());
			int index = revisions.indexOf(revision);
			if (index == 0) {
				return null;
			} else {
				return revisions.get(--index);
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Transactional(readOnly = true)
	public List<?> getRevisionDetails(Class<?> clazz, Integer entityId, Integer revisionId) {
		log.debug("Getting revision details [{}] for entity [{}] with ID [{}]", revisionId, clazz.getSimpleName(), entityId);
		List<?> objects = AuditReaderFactory.get(entityManager).createQuery().forEntitiesAtRevision(clazz, revisionId).add(new IdentifierEqAuditExpression(entityId, true)).getResultList();
		return objects;
	}

}
