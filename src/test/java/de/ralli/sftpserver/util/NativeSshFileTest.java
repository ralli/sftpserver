package de.ralli.sftpserver.util;

import org.apache.sshd.server.filesystem.NativeSshFile;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NativeSshFileTest {
	private static final Logger logger = LoggerFactory.getLogger(NativeSshFileTest.class);
	
	@Test
	public void test1() {
		String result = NativeSshFile.getPhysicalName("/test/wunder/hase/", "", "../ralli/test.txt");
		logger.info("result = {}", result);
	}
	
	@Test
	public void test2() {
		logger.info("user.dir={}", System.getProperty("user.dir"));
	}
}
