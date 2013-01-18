package de.ralli.sftpserver.core.util;

import java.security.Key;

public interface KeySerializer {

    public String getFingerprintString(Key key);
    
    public String getFingerprintString(byte[] keydata);
    
    byte[] getFingerprint(byte[] keydata);

    byte[] getFingerprint(Key key);

    String toOpenSSHString();

    String toSecSSHString();

    void writeAsOpenSSH(String fileName, Key key);

    void writeAsSecSSH(String fileName, Key key);
}
