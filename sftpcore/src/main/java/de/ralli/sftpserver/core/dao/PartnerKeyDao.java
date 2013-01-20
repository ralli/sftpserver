package de.ralli.sftpserver.core.dao;

import de.ralli.sftpserver.core.entities.PartnerKey;

public interface PartnerKeyDao {
    PartnerKey findByLoginAndFingerPrint(String login, String fingerPrint);

    void insert(PartnerKey key);

    void update(PartnerKey key);

    void delete(PartnerKey key);
}
