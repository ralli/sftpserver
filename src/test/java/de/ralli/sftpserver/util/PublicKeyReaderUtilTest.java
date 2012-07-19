package de.ralli.sftpserver.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublicKeyReaderUtilTest {
	private static final Logger logger = LoggerFactory
			.getLogger(PublicKeyReaderUtilTest.class);

	private String loadPublicKey() throws Exception {
		InputStream is = getClass().getResourceAsStream("/id_pub.ppk");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		char[] buf = new char[1024];
		int count;
		StringWriter out = new StringWriter();

		while ((count = in.read(buf, 0, buf.length)) >= 0) {
			out.write(buf, 0, count);
		}

		String key = out.toString();
		logger.info("key={}", key);
		return key;
	}

	@Test
	public void testLoadPublicKey() throws Exception {
		String key = loadPublicKey();
		PublicKey publicKey = PublicKeyReaderUtil.load(key);
		logger.info("public key: {}", publicKey);
		logger.info("openssh: {}", PublicKeyReaderUtil.encodeToOpenSSH((RSAPublicKey) publicKey)); 
	}
}
