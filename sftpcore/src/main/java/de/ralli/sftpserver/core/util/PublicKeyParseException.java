package de.ralli.sftpserver.core.util;

/**
 * The Exception is throws if the public key encoded text could not be parsed.
 * For the related {@link PublicKeyParseException#errorCode} see enumeration
 * null {@link PublicKeyParseException .ErrorCode}.
 */
public class PublicKeyParseException extends RuntimeException {

	/**
	 * Defines the serialize version unique identifier.
	 */
	private static final long serialVersionUID = 1446034172449421912L;
	/**
	 * Error code of the public key parse exception.
	 */
	private final PublicKeyErrorCode errorCode;

	/**
	 * Creates a new exception for defined <code>_errorCode</code>.
	 * 
	 * @param _errorCode
	 *            error code
	 */
	public PublicKeyParseException(final PublicKeyErrorCode _errorCode) {
		super(_errorCode.getMessage());
		this.errorCode = _errorCode;
	}

	public PublicKeyParseException(final PublicKeyErrorCode _errorCode, Exception cause) {
		super(_errorCode.getMessage(), cause);
		this.errorCode = _errorCode;
	}

	/**
	 * Creates a new exception for defined <code>_errorCode</code> and
	 * <code>_cause</code>.
	 * 
	 * @param _errorCode
	 *            error code
	 * @param _cause
	 *            throwable clause
	 */
	public PublicKeyParseException(final PublicKeyErrorCode _errorCode,
			final Throwable _cause) {
		super(_errorCode.getMessage(), _cause);
		this.errorCode = _errorCode;
	}

	/**
	 * Returns the error code enumeration of this public key parse exception
	 * instance.
	 * 
	 * @return error code of the public key parse exception instance
	 * @see #errorCode
	 */
	public PublicKeyErrorCode getErrorCode() {
		return this.errorCode;
	}
}