package com.galaxe.gxworkflow.exception;

/**
 * RequestDataNotFoundException is a runtime exception that is thrown when a
 * specific error condition occurs in the application.
 * 
 * <p>
 * It extends the RuntimeException class, making it an unchecked exception that
 * does not require explicit handling.
 * 
 * It is thrown when no request is obtained from the user.
 */
public class RequestDataNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RequestDataNotFoundException() {

	}

	/**
	 * Constructs a new DataNotFoundException with the specified error message.
	 *
	 * @param message the error message that describes the specific exception
	 *                condition.
	 */
	public RequestDataNotFoundException(String message) {
		super(message);

	}

	public RequestDataNotFoundException(Throwable cause) {
		super(cause);

	}

	public RequestDataNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public RequestDataNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

}
