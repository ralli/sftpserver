package de.ralli.sftpserver.filesystem;

import org.apache.sshd.common.Session;
import org.apache.sshd.server.FileSystemView;
import org.apache.sshd.server.filesystem.NativeFileSystemFactory;

public class SimpleFileSystemFactory extends NativeFileSystemFactory {
	@Override
	public FileSystemView createFileSystemView(Session session) {
		final String userName = session.getUsername();
		return new SimpleFileSystemView(userName);
	}
}
