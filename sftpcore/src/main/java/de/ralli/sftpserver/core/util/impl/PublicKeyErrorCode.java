package de.ralli.sftpserver.core.util.impl;

/**
 * Enumeration of the error codes if the public key could not parsed.
 */
public enum PublicKeyErrorCode {

    /**
     * The format of the given ASCII key is not known and could not be parsed.
     * Only OpenSSH (starts with 's') and SECSH (starts with '-') are currently
     * supported.
     *
     * @see PublicKeyReaderUtil#load(String)
     */
    UNKNOWN_PUBLIC_KEY_FILE_FORMAT(
    "Corrupt or unknown public key file format"),
    /**
     * The binary blob of the key definition used a not supported public key
     * certificate format. Only DSA and RSA are currently supported.
     *
     * @see PublicKeyReaderUtil#SSH2_DSA_KEY
     * @see PublicKeyReaderUtil#SSH2_RSA_KEY
     * @see PublicKeyReaderUtil#load(String)
     */
    UNKNOWN_PUBLIC_KEY_CERTIFICATE_FORMAT(
    "Corrupt or unknown public key certificate format"),
    /**
     * The public key string is not defined correctly in OpenSSH format.
     *
     * @see PublicKeyReaderUtil#extractOpenSSHBase64(String)
     */
    CORRUPT_OPENSSH_PUBLIC_KEY_STRING(
    "Corrupt OpenSSH public key string"),
    /**
     * The public key string is not defined correctly in SECSSH format.
     *
     * @see PublicKeyReaderUtil#extractSecSHBase64(String)
     */
    CORRUPT_SECSSH_PUBLIC_KEY_STRING("Corrupt SECSSH public key string"),
    /**
     * The DSA public key blob could not decoded.
     *
     * @see PublicKeyReaderUtil#decodeDSAPublicKey(SSH2DataBuffer)
     */
    SSH2DSA_ERROR_DECODING_PUBLIC_KEY_BLOB(
    "SSH2DSA: error decoding public key blob"),
    /**
     * The RSA public key blob could not decoded.
     *
     * @see PublicKeyReaderUtil#decodeRSAPublicKey(SSH2DataBuffer)
     */
    SSH2RSA_ERROR_DECODING_PUBLIC_KEY_BLOB(
    "SSH2RSA: error decoding public key blob"),
    /**
     * @see PublicKeyReaderUtil.SSH2DataBuffer#readByteArray()
     */
    CORRUPT_BYTE_ARRAY_ON_READ("Corrupt byte array on read"),
    UNKNOWN_PUBLIC_KEY_CLASS(
    "Unknown public key class (only DSA and RSA supported)");
    /**
     * English message of the error code.
     */
    private final String message;

    /**
     * Constructor used to initialize the error codes with an error message.
     *
     * @param _message message text of the error code
     */
    PublicKeyErrorCode(final String _message) {
        this.message = _message;
    }

    public String getMessage() {
        return message;
    }
}
