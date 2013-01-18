package de.ralli.sftpserver.util;

import java.io.FileInputStream;
import java.security.PublicKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPublicKey;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.ralli.sftpserver.core.util.KeySerializerImpl;

public class KeySerializerTest {
	private static final String DSA_FINGERPRINT = "61:58:a7:12:63:d3:59:ec:6a:fa:5e:87:e7:31:26:29";
	private static final String RSA_FINGERPRINT = "08:9d:78:61:87:57:71:dc:32:fe:59:60:ba:26:ad:f8";
	private KeySerializerImpl keySerializer;

	@Before
	public void setUp() {
		keySerializer = new KeySerializerImpl();
	}

	private PublicKey loadPublicKey(String resourceName) throws Exception {
		String file = getClass().getResource(resourceName).getFile();
		FileInputStream in = new FileInputStream(file);
		try {
			PublicKey key = keySerializer.loadPublicKey(in);
			return key;
		} finally {
			in.close();
		}
	}

	@Test
	public void testLoadRSAPublicKey() throws Exception {
		PublicKey key = loadPublicKey("/testkey.pub");
		Assert.assertTrue(key instanceof RSAPublicKey);
	}

	@Test
	public void testLoadDSAPublicKey() throws Exception {
		PublicKey key = loadPublicKey("/id_dsa.pub");
		Assert.assertTrue(key instanceof DSAPublicKey);
	}

	@Test
	public void testRSAFingerprint() throws Exception {
		PublicKey key = loadPublicKey("/testkey.pub");
		Assert.assertEquals(RSA_FINGERPRINT,
				keySerializer.getFingerprintString(key));
	}

	@Test
	public void testDSAFingerprint() throws Exception {
		PublicKey key = loadPublicKey("/id_dsa.pub");
		Assert.assertEquals(DSA_FINGERPRINT, keySerializer.getFingerprintString(key));
	}
}
