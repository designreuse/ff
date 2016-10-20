package org.ff.jpa.specification;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.ff.jpa.SearchCriteria;
import org.ff.jpa.domain.Email;
import org.ff.jpa.domain.Tender;
import org.ff.jpa.domain.User;
import org.ff.jpa.domain.UserEmail;
import org.springframework.data.jpa.domain.Specification;

public class UserEmailSpecification implements Specification<UserEmail> {

	private SearchCriteria criteria;

	public UserEmailSpecification(final SearchCriteria criteria) {
		super();
		this.criteria = criteria;
	}

	public SearchCriteria getCriteria() {
		return criteria;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Predicate toPredicate(Root<UserEmail> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		switch (criteria.getOperation()) {
			case EQUALITY:
				if (criteria.getKey().equals("tender.id")) {
					return builder.equal(root.<Tender>get("tender").<Integer>get("id"), criteria.getValue());
				} else if (criteria.getKey().equals("user.id")) {
					return builder.equal(root.<User>get("user").<Integer>get("id"), criteria.getValue());
				}
				return builder.equal(root.get(criteria.getKey()), criteria.getValue());
			case NEGATION:
				return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
			case GREATER_THAN:
				return builder.greaterThan(root.<String> get(criteria.getKey()), criteria.getValue().toString());
			case LESS_THAN:
				return builder.lessThan(root.<String> get(criteria.getKey()), criteria.getValue().toString());
			case LIKE:
				return builder.like(root.<String> get(criteria.getKey()), criteria.getValue().toString());
			case STARTS_WITH:
				return builder.like(root.<String> get(criteria.getKey()), criteria.getValue() + "%");
			case ENDS_WITH:
				return builder.like(root.<String> get(criteria.getKey()), "%" + criteria.getValue());
			case CONTAINS:
				if (criteria.getKey().equals("user.email")) {
					return builder.like(root.<User> get("user").<String>get("email"), "%" + criteria.getValue() + "%");
				} else if (criteria.getKey().equals("email.subject")) {
					return builder.like(root.<Email> get("email").<String>get("subject"), "%" + criteria.getValue() + "%");
				}
				return builder.like(root.<String> get(criteria.getKey()), "%" + criteria.getValue() + "%");
			case BETWEEN:
				List<Date> range = (List<Date>) criteria.getValue();
				return builder.between(root.<Date> get(criteria.getKey()), range.get(0), range.get(1));
			default:
				return null;
		}
	}

}