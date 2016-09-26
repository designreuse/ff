package org.ff.controller;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.ff.exception.ValidationFailedException;

public class BaseController {

	protected RuntimeException processException(RuntimeException e) {
		if (e instanceof ConstraintViolationException) {
			return processConstraintViolationException((ConstraintViolationException) e);
		}

		Throwable t = e.getCause();
		while ((t != null) && !(t instanceof ConstraintViolationException)) {
			t = t.getCause();
		}

		if (t instanceof ConstraintViolationException) {
			return processConstraintViolationException((ConstraintViolationException) t);
		}

		return e;
	}

	protected ValidationFailedException processConstraintViolationException(ConstraintViolationException cve) {
		StringBuilder sb = new StringBuilder();
		for (ConstraintViolation<?> constraintViolation : cve.getConstraintViolations()) {
			sb.append("• ").append(constraintViolation.getMessage()).append("<br>");
		}
		return new ValidationFailedException(sb.toString());
	}

}
