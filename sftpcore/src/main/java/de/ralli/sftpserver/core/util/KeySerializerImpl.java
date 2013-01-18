package de.ralli.sftpserver.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.commons.codec.binary.Base64;

public class KeySerializerImpl implements KeySerializer {

	/**
	 * Begin marker for the SECSH public key file format.
	 * 
	 * @see #extractSecSHBase64(String)
	 */
	private static final String BEGIN_PUB_KEY = "---- BEGIN SSH2 PUBLIC KEY ----";

	/**
	 * End marker for the SECSH public key file format.
	 * 
	 * @see #extractSecSHBase64(String)
	 */
	private static final String END_PUB_KEY = "---- END SSH2 PUBLIC KEY ----";

	/**
	 * Key name of the type of public key for DSA algorithm.
	 * 
	 * @see #loadPublicKey(String)
	 */
	private static final String SSH2_DSA_KEY = "ssh-dss";

	/**
	 * Key name of the type of public key for RSA algorithm.
	 * 
	 * @see #loadPublicKey(String)
	 */
	private static final String SSH2_RSA_KEY = "ssh-rsa";

	private byte[] SSH_RSA = { 0, 0, 0, 7, 's', 's', 'h', '-', 'r', 's', 'a' };
	private byte[] SSH_DSA = { 0, 0, 0, 7, 's', 's', 'h', '-', 'd', 's', 'a' };

	@Override
	public String getFingerprintString(Key key) {
		byte[] bytes = getFingerprint(key);
		return getFingerprintString(bytes);
	}

	@Override
	public String getFingerprintString(byte[] bytes) {
		boolean first = true;
		StringBuilder out = new StringBuilder();
		for (byte b : bytes) {
			if (!first) {
				out.append(':');
			}
			first = false;
			out.append(String.format("%02x", b & 0xFF));
		}
		return out.toString();
	}

	@Override
	public byte[] getFingerprint(byte[] keydata) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(keydata);
			return thedigest;
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}

	@Override
	public byte[] getFingerprint(Key key) {
		String algorithm = key.getAlgorithm();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		if (!"RSA".equals(algorithm) && !"DSA".equals(algorithm)) {
			throw new RuntimeException("Unknown Algorithm " + algorithm);
		}

		try {
			byte[] bytes = encodePublicKey((PublicKey) key);
			out.write(bytes);

			return getFingerprint(out.toByteArray());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public String toOpenSSHString() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String toSecSSHString() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public PublicKey loadPublicKey(final String _key)
			throws PublicKeyParseException {
		final int c = _key.charAt(0);

		final String base64;

		if (c == 's') {
			base64 = extractOpenSSHBase64(_key);
		} else if (c == '-') {
			base64 = extractSecSHBase64(_key);
		} else {
			throw new PublicKeyParseException(
					PublicKeyErrorCode.UNKNOWN_PUBLIC_KEY_FILE_FORMAT);
		}

		final SSHByteArraySerializer buf = new SSHByteArraySerializer(
				Base64.decodeBase64(base64.getBytes()));
		final String type = buf.readString();
		final PublicKey ret;
		if (SSH2_DSA_KEY.equals(type)) {
			ret = decodeDSAPublicKey(buf);
		} else if (SSH2_RSA_KEY.equals(type)) {
			ret = decodePublicKey(buf);
		} else {
			throw new PublicKeyParseException(
					PublicKeyErrorCode.UNKNOWN_PUBLIC_KEY_CERTIFICATE_FORMAT);
		}

		return ret;
	}

	private String loadFile(InputStream in) throws IOException {
		byte[] buf = new byte[1024];
		int n;
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		while ((n = in.read(buf)) > 0) {
			out.write(buf, 0, n);
		}
		return out.toString();
	}

	@Override
	public PublicKey loadPublicKey(InputStream in) {
		try {
			String contents = loadFile(in);
			PublicKey result = loadPublicKey(contents);
			return result;
		} catch (IOException ex) {
			throw new PublicKeyParseException(
					PublicKeyErrorCode.UNKNOWN_PUBLIC_KEY_FILE_FORMAT, ex);
		}
	}

	@Override
	public void writeAsOpenSSH(String fileName, Key key) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void writeAsSecSSH(String fileName, Key key) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private byte[] encodePublicKey(PublicKey key) throws Exception {
		byte[] result = null;

		if (key instanceof RSAPublicKey) {
			result = encodeRSAPublicKey((RSAPublicKey) key);
		} else if (key instanceof DSAPublicKey) {
			result = encodeDSAPublicKey((DSAPublicKey) key);
		} else {
			throw new PublicKeyParseException(
					PublicKeyErrorCode.UNKNOWN_PUBLIC_KEY_CLASS);
		}
		return result;
	}

	private byte[] encodeRSAPublicKey(RSAPublicKey key) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		out.write(SSH_RSA);
		/* Encode the public exponent */
		BigInteger e = key.getPublicExponent();
		byte[] data = e.toByteArray();
		encodeUInt32(data.length, out);
		out.write(data);
		/* Encode the modulus */
		BigInteger m = key.getModulus();
		data = m.toByteArray();
		encodeUInt32(data.length, out);
		out.write(data);
		return out.toByteArray();
	}

	private byte[] encodeDSAPublicKey(DSAPublicKey key) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(SSH_DSA);
		DSAParams params = key.getParams();
		byte[] data = params.getP().toByteArray();
		encodeUInt32(data.length, out);
		out.write(data);

		data = params.getQ().toByteArray();
		encodeUInt32(data.length, out);
		out.write(data);

		data = params.getG().toByteArray();
		encodeUInt32(data.length, out);
		out.write(data);

		data = key.getY().toByteArray();
		encodeUInt32(data.length, out);
		out.write(data);

		return out.toByteArray();
	}

	private void encodeUInt32(int value, OutputStream out) throws IOException {
		byte[] tmp = new byte[4];
		tmp[0] = (byte) ((value >>> 24) & 0xff);
		tmp[1] = (byte) ((value >>> 16) & 0xff);
		tmp[2] = (byte) ((value >>> 8) & 0xff);
		tmp[3] = (byte) (value & 0xff);
		out.write(tmp);
	}

	/**
	 * <p>
	 * Extracts from the OpenSSH public key format the base64 encoded SSH public
	 * key.
	 * </p>
	 * <p>
	 * An example of such a definition is:<br/>
	 * <code>ssh-rsa AAAAB3NzaC1yc2EAAAABIwAAAIEA1on8gxCGJJWSRT4uOrR130....</code>
	 * </p>
	 * 
	 * @param _key
	 *            text of the public key defined in the OpenSSH format
	 * @return base64 encoded public-key data
	 * @throws PublicKeyParseException
	 *             if the OpenSSH public key string is corrupt
	 * @see PublicKeyParseException.ErrorCode#CORRUPT_OPENSSH_PUBLIC_KEY_STRING
	 * @see <a href="http://www.openssh.org">OpenSSH</a>
	 */
	public String extractOpenSSHBase64(final String _key)
			throws PublicKeyParseException {
		final String base64;
		try {
			final StringTokenizer st = new StringTokenizer(_key);
			st.nextToken();
			base64 = st.nextToken();
		} catch (final NoSuchElementException e) {
			throw new PublicKeyParseException(
					PublicKeyErrorCode.CORRUPT_OPENSSH_PUBLIC_KEY_STRING);
		}

		return base64;
	}

	/**
	 * <p>
	 * Extracts from the SECSSH public key format the base64 encoded SSH public
	 * key.
	 * </p>
	 * <p>
	 * An example of such a definition is:
	 * 
	 * <pre>
	 *  ---- BEGIN SSH2 PUBLIC KEY ----
	 * Comment: This is my public key for use on \
	 * servers which I don't like.
	 * AAAAB3NzaC1kc3MAAACBAPY8ZOHY2yFSJA6XYC9HRwNHxaehvx5wOJ0rzZdzoSOXxbET
	 * W6ToHv8D1UJ/z+zHo9Fiko5XybZnDIaBDHtblQ+Yp7StxyltHnXF1YLfKD1G4T6JYrdH
	 * YI14Om1eg9e4NnCRleaqoZPF3UGfZia6bXrGTQf3gJq2e7Yisk/gF+1VAAAAFQDb8D5c
	 * vwHWTZDPfX0D2s9Rd7NBvQAAAIEAlN92+Bb7D4KLYk3IwRbXblwXdkPggA4pfdtW9vGf
	 * J0/RHd+NjB4eo1D+0dix6tXwYGN7PKS5R/FXPNwxHPapcj9uL1Jn2AWQ2dsknf+i/FAA
	 * vioUPkmdMc0zuWoSOEsSNhVDtX3WdvVcGcBq9cetzrtOKWOocJmJ80qadxTRHtUAAACB
	 * AN7CY+KKv1gHpRzFwdQm7HK9bb1LAo2KwaoXnadFgeptNBQeSXG1vO+JsvphVMBJc9HS
	 * n24VYtYtsMu74qXviYjziVucWKjjKEb11juqnF0GDlB3VVmxHLmxnAz643WK42Z7dLM5
	 * sY29ouezv4Xz2PuMch5VGPP+CDqzCM4loWgV
	 * ---- END SSH2 PUBLIC KEY ----
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param _key
	 *            text of the public key defined in the SECSH format
	 * @return base64 encoded public-key data
	 * @throws PublicKeyParseException
	 *             if the SECSSH key text file is corrupt
	 * @see PublicKeyParseException.ErrorCode#CORRUPT_SECSSH_PUBLIC_KEY_STRING
	 * @see <a
	 *      href="http://tools.ietf.org/html/draft-ietf-secsh-publickeyfile">IETF
	 *      Draft for the SECSH format</a>
	 */
	private String extractSecSHBase64(final String _key)
			throws PublicKeyParseException {
		final StringBuilder base64Data = new StringBuilder();

		boolean startKey = false;
		boolean startKeyBody = false;
		boolean endKey = false;
		boolean nextLineIsHeader = false;
		for (final String line : _key.split("\n")) {
			final String trimLine = line.trim();
			if (!startKey && trimLine.equals(BEGIN_PUB_KEY)) {
				startKey = true;
			} else if (startKey) {
				if (trimLine.equals(END_PUB_KEY)) {
					endKey = true;
					break;
				} else if (nextLineIsHeader) {
					if (!trimLine.endsWith("\\")) {
						nextLineIsHeader = false;
					}
				} else if (trimLine.indexOf(':') > 0) {
					if (startKeyBody) {
						throw new PublicKeyParseException(
								PublicKeyErrorCode.CORRUPT_SECSSH_PUBLIC_KEY_STRING);
					} else if (trimLine.endsWith("\\")) {
						nextLineIsHeader = true;
					}
				} else {
					startKeyBody = true;
					base64Data.append(trimLine);
				}
			}
		}

		if (!endKey) {
			throw new PublicKeyParseException(
					PublicKeyErrorCode.CORRUPT_SECSSH_PUBLIC_KEY_STRING);
		}

		return base64Data.toString();
	}

	/**
	 * <p>
	 * Decodes a DSA public key according to the SSH standard from the data
	 * <code>_buffer</code> based on <b>NIST's FIPS-186</b>. The values of the
	 * DSA public key specification are read in the order
	 * <ul>
	 * <li>prime p</li>
	 * <li>sub-prime q</li>
	 * <li>base g</li>
	 * <li>public key y</li>
	 * </ul>
	 * With the specification the related DSA public key is generated.
	 * </p>
	 * 
	 * @param _buffer
	 *            SSH2 data buffer where the type of the key is already read
	 * @return DSA public key instance
	 * @throws PublicKeyParseException
	 *             if the SSH2 public key blob could not be decoded
	 * @see DSAPublicKeySpec
	 * @see <a
	 *      href="http://en.wikipedia.org/wiki/Digital_Signature_Algorithm">Digital
	 *      Signature Algorithm on Wikipedia</a>
	 * @see <a href="http://tools.ietf.org/html/rfc4253#section-6.6">RFC 4253
	 *      Section 6.6</a>
	 */
	private PublicKey decodeDSAPublicKey(final SSHByteArraySerializer _buffer)
			throws PublicKeyParseException {
		final BigInteger p = _buffer.readMPint();
		final BigInteger q = _buffer.readMPint();
		final BigInteger g = _buffer.readMPint();
		final BigInteger y = _buffer.readMPint();

		try {
			final KeyFactory dsaKeyFact = KeyFactory.getInstance("DSA");
			final DSAPublicKeySpec dsaPubSpec = new DSAPublicKeySpec(y, p, q, g);

			return dsaKeyFact.generatePublic(dsaPubSpec);

		} catch (final Exception e) {
			throw new PublicKeyParseException(
					PublicKeyErrorCode.SSH2DSA_ERROR_DECODING_PUBLIC_KEY_BLOB,
					e);
		}
	}

	/**
	 * <p>
	 * Decode a RSA public key encoded according to the SSH standard from the
	 * data <code>_buffer</code>. The values of the RSA public key specification
	 * are read in the order
	 * <ul>
	 * <li>public exponent</li>
	 * <li>modulus</li>
	 * </ul>
	 * With the specification the related RSA public key is generated.
	 * </p>
	 * 
	 * @param _buffer
	 *            key / certificate data (certificate or public key format
	 *            identifier is already read)
	 * @return RSA public key instance
	 * @throws PublicKeyParseException
	 *             if the SSH2 public key blob could not be decoded
	 * @see RSAPublicKeySpec
	 * @see <a href="http://en.wikipedia.org/wiki/RSA">RSA on Wikipedia</a>
	 * @see <a href="http://tools.ietf.org/html/rfc4253#section-6.6">RFC 4253
	 *      Section 6.6</a>
	 */
	private PublicKey decodePublicKey(final SSHByteArraySerializer _buffer)
			throws PublicKeyParseException {
		final BigInteger e = _buffer.readMPint();
		final BigInteger n = _buffer.readMPint();

		try {
			final KeyFactory rsaKeyFact = KeyFactory.getInstance("RSA");
			final RSAPublicKeySpec rsaPubSpec = new RSAPublicKeySpec(n, e);

			return rsaKeyFact.generatePublic(rsaPubSpec);

		} catch (final Exception ex) {
			throw new PublicKeyParseException(
					PublicKeyErrorCode.SSH2RSA_ERROR_DECODING_PUBLIC_KEY_BLOB,
					ex);
		}
	}

}
