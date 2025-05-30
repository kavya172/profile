package com.galaxe.gxworkflow.exception;

/**
 * DataNotFoundException is a runtime exception that is thrown when a specific
 * error condition occurs in the application.
 * 
 * <p>
 * It extends the RuntimeException class, making it an unchecked exception that
 * does not require explicit handling.
 * 
 * It is thrown when there there is no available Data to display.
 */
public class DataNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataNotFoundException() {

	}

	/**
	 * Constructs a new DataNotFoundException with the specified error message.
	 *
	 * @param message the error message that describes the specific exception
	 *                condition.
	 */
	public DataNotFoundException(String message) {
		super(message);

	}

	public DataNotFoundException(Throwable cause) {
		super(cause);

	}

	public DataNotFoundException(String message, Throwable cause) {
		super(message, cause);

	}

	public DataNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
