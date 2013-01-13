package de.ralli.sftpserver.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.KeyPair;

public class KeyTest {
	private static final Logger log = LoggerFactory.getLogger(KeyTest.class);

	@Test
	public void testKeyGeneration() throws Exception {
		URL uri = getClass().getResource("/testkey");
		File file = new File(uri.getPath());
		JSch jsch = new JSch();
		KeyPair keyPair = KeyPair.load(jsch, file.getCanonicalPath());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		keyPair.writePrivateKey(out);
		log.info("\n{}", out.toString());
	}
}
