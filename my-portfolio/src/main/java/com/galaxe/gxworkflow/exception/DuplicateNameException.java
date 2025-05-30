package com.galaxe.gxworkflow.exception;

/**
 * DuplicateNameException is a runtime exception that is thrown when a specific
 * error condition occurs in the application.
 * 
 * <p>
 * It extends the RuntimeException class, making it an unchecked exception that
 * does not require explicit handling.
 * 
 * It is thrown when similar name already exists.
 */
public class DuplicateNameException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateNameException() {

	}

	/**
	 * Constructs a new DuplicateNameException with the specified error message.
	 *
	 * @param message the error message that describes the specific exception
	 *                condition.
	 */
	public DuplicateNameException(String message) {
		super(message);

	}

	public DuplicateNameException(Throwable cause) {
		super(cause);

	}

	public DuplicateNameException(String message, Throwable cause) {
		super(message, cause);

	}

	public DuplicateNameException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

}
