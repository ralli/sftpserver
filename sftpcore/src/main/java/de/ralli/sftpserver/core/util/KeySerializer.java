package de.ralli.sftpserver.core.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.PublicKey;

/**
 * Public key serialization and deserialization. Provides functionality to
 * compute MD5 fingerprints.
 */
public interface KeySerializer {
	/**
	 * Returns a Public Key fingerprint as a Hex-String separated by colons ':'.
	 * The Fingerprint is computed as MD5-Checksum over the key bits as encoded
	 * by <code>encodePublicKey</code> which is the same as
	 * <code>ssh-keygen -l -f &lt;file&gt;</code> returns.
	 * <p>
	 * Example: Given the RSA-Public Key
	 * <code>ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDtR5Akx/srAhfSA+Oy9D8vr6MyKQhKLj1D7nV+6W1SSEWew+FmNnYi+vxfht4h/sW2rLZf5jLM5tndtTfyqA7iCJcMvrh6PqlVtKYMzAf3yVIT0/DizYbiMFaL76Ziu5NLHD6bpTL8GEtVDpyzaSjPD/ohrgVjlrHyBC/Ori0DbnI6eQxQV6O1GnIPzq+aCasT0YoIBqUZfzfDUZJN9mxzNnZLR09AAD3aYNBmK7F0Iykixr9vc7Mm5na9+4klbnwMznTEYXoCphFDuO/jEhxOT+WL/l2yGszxSfqW3N6OO5SVM4G63Q0z0eW2RgYOCCy5K39OULXXyzmovZEXhmMl ralli@dahaam</code>
	 * This method returns
	 * <code>08:9d:78:61:87:57:71:dc:32:fe:59:60:ba:26:ad:f8</code>
	 * </p>
	 * 
	 * @param key
	 *            The key to get the fingerprint for.
	 * @return the fingerprint as String
	 */
	public String getFingerprintString(PublicKey key);

	/**
	 * Returns a Public Key fingerprint as a Hex-String separated by colons ':'.
	 * The Fingerprint is computed as MD5-Checksum over the key bits as encoded
	 * by <code>encodePublicKey</code>.
	 * 
	 * @see KeySerializer.getFingerprintString(PublicKey key)
	 * @param keydata
	 *            the data of the key as returned by
	 *            <code>encodePublicKey(PublicKey
	 *            key)</code>
	 * @return the fingerprint as String
	 */
	public String getFingerprintString(byte[] keydata);

	/**
	 * Returns the public key fingerprint.
	 * 
	 * @see KeySerializer.getFingerprintString(PublicKey key)
	 * @param keydata
	 *            the data of the key as returned by
	 *            <code>encodePublicKey(PublicKey
	 *            key)</code>
	 * @return the fingerprint as byte array
	 */
	byte[] getFingerprint(byte[] keydata);

	/**
	 * Returns the public key fingerprint.
	 * 
	 * @see KeySerializer.getFingerprintString(PublicKey key)
	 * 
	 * @param key
	 *            The key to get the fingerprint for.
	 * @return the fingerprint as byte array
	 */
	byte[] getFingerprint(PublicKey key);

	/**
	 * 
	 * @return
	 */
	String toOpenSSHString(PublicKey key);

	String toSecSSHString(PublicKey key);

	byte[] encodePublicKey(PublicKey key);

	PublicKey loadPublicKey(InputStream in);

	PublicKey loadPublicKey(final String _key);

	void writeAsOpenSSH(OutputStream out, PublicKey key);

	void writeAsSecSSH(OutputStream out, PublicKey key);
}
