package com.sig.camunda.bpm_dto;

public class InternalException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InternalException() {
	}

	public InternalException(String message) {
		super(message);
	}

	public InternalException(String message, Throwable cause) {
		super(message, cause);
	}

	public InternalException(Throwable cause) {
		super(cause);
	}

	protected InternalException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
