package de.ralli.sftpserver.core.util;

import java.io.InputStream;
import java.security.Key;
import java.security.PublicKey;

public interface KeySerializer {

	public String getFingerprintString(Key key);

	public String getFingerprintString(byte[] keydata);

	byte[] getFingerprint(byte[] keydata);

	byte[] getFingerprint(Key key);

	String toOpenSSHString();

	String toSecSSHString();

	PublicKey loadPublicKey(InputStream in);

	PublicKey loadPublicKey(final String _key);

	void writeAsOpenSSH(String fileName, Key key);

	void writeAsSecSSH(String fileName, Key key);
}
