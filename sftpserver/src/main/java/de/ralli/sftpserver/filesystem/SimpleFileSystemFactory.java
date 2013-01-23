package de.ralli.sftpserver.filesystem;

import org.apache.sshd.common.Session;
import org.apache.sshd.server.FileSystemView;
import org.apache.sshd.server.filesystem.NativeFileSystemFactory;

public class SimpleFileSystemFactory extends NativeFileSystemFactory {
    private String baseDir;

    public SimpleFileSystemFactory(String baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public FileSystemView createFileSystemView(Session session) {
        final String userName = session.getUsername();
        return new SimpleFileSystemView(userName, baseDir);
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }
}
