package de.ralli.sftpserver.filesystem;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.sshd.server.FileSystemView;
import org.apache.sshd.server.SshFile;
import org.apache.sshd.server.filesystem.NativeFileSystemFactory;
import org.apache.sshd.server.filesystem.NativeSshFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleFileSystemView implements FileSystemView {

    private final Logger LOG = LoggerFactory.getLogger(SimpleFileSystemView.class);

    // the first and the last character will always be '/'
    // It is always with respect to the root directory.
    private String currDir;

    private String userName;

    private boolean caseInsensitive = false;

    /**
     * The base directory.
     * 
     * All Users will have their root directory relative to that path
     */
    private String baseDir;

    /**
     * Constructor - internal do not use directly, use
     * {@link NativeFileSystemFactory} instead
     */
    protected SimpleFileSystemView(String userName, String baseDir) {
        this(userName, baseDir, false);
    }

    /**
     * Constructor - internal do not use directly, use
     * {@link NativeFileSystemFactory} instead
     */
    public SimpleFileSystemView(String userName, String baseDir, boolean caseInsensitive) {
        if (userName == null) {
            throw new IllegalArgumentException("user can not be null");
        }

        this.baseDir = baseDir;
        this.caseInsensitive = caseInsensitive;

        currDir = baseDir;
        this.userName = userName;

        // add last '/' if necessary
        LOG.debug("Simple filesystem view created for user \"{}\" with root \"{}\"", userName, currDir);
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    /**
     * Get file object.
     */
    public SshFile getFile(String file) {
        return getFile(currDir, file);
    }

    public SshFile getFile(SshFile baseDir, String file) {
        return getFile(baseDir.getAbsolutePath(), file);
    }

    protected SshFile getFile(String dir, String file) {
        String physicalDirName = NativeSshFile.getPhysicalName("/",
                dir, ".", caseInsensitive);
        String myDir = StringUtils.removeStart(physicalDirName, baseDir);
        String physicalName = NativeSshFile.getPhysicalName(baseDir, myDir, file, caseInsensitive);
        File fileObj = new File(physicalName);

        // strip the root directory and return
        String userFileName = NativeSshFile.getPhysicalName("/", myDir, file, caseInsensitive);
        return new SimpleSshFile(userFileName, fileObj, userName);
    }
}
