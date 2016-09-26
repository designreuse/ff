package org.ff.exception;

public class ValidationFailedException extends RuntimeException {

	private static final long serialVersionUID = 8109098651187553668L;

	public ValidationFailedException() {
		super();
	}

	public ValidationFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ValidationFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidationFailedException(String message) {
		super(message);
	}

	public ValidationFailedException(Throwable cause) {
		super(cause);
	}

}
