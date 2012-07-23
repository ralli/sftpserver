package de.ralli.sftpserver.filesystem;

import java.io.File;

import org.apache.sshd.server.filesystem.NativeSshFile;

public class SimpleSshFile extends NativeSshFile {
	public SimpleSshFile(final String fileName, final File file,
			final String userName) {
		super(fileName, file, userName);
	}
}
