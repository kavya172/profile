package com.galaxe.gxworkflow.exception;

public class InValidDataException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InValidDataException() {

	}
	
	public InValidDataException(String message) {
		super(message);

	}

	public InValidDataException(Throwable cause) {
		super(cause);

	}

	public InValidDataException(String message, Throwable cause) {
		super(message, cause);

	}

	public InValidDataException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

}
