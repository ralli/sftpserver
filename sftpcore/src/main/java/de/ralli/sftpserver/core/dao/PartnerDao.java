package de.ralli.sftpserver.core.dao;

import org.springframework.stereotype.Service;

import de.ralli.sftpserver.core.entities.Partner;

@Service 
public interface PartnerDao {
    Partner findByLogin(String login);

    void insert(Partner partner);

    void update(Partner partner);

    void delete(Partner partner);
}
