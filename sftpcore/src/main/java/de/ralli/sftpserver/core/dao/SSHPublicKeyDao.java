package de.ralli.sftpserver.core.dao;

import de.ralli.sftpserver.core.entities.SSHPublicKey;

public interface SSHPublicKeyDao {
    SSHPublicKey findByFingerPrint(String fingerPrint);
    void insert(SSHPublicKey publicKey);
    void update(SSHPublicKey publicKey);
    void delete(SSHPublicKey publicKey);
}
