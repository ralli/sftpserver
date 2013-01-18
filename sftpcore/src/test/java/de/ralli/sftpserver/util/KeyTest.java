package de.ralli.sftpserver.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.KeyPair;

public class KeyTest {
	private String loadFile(String fileName) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n;
		FileInputStream in = new FileInputStream(fileName);
		try {
			while ((n = in.read(buf)) > 0) {
				out.write(buf, 0, n);
			}
		} finally {
			in.close();
		}
		return out.toString();
	}

	@Test
	public void testKeyGeneration() throws Exception {
		URL uri = getClass().getResource("/testkey");		
		File file = new File(uri.getPath());
		JSch jsch = new JSch();
		KeyPair keyPair = KeyPair.load(jsch, file.getCanonicalPath());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		keyPair.writePrivateKey(out);		
		Assert.assertEquals(loadFile(file.getCanonicalPath()), out.toString());
	}
}
