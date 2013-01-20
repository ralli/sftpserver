package de.ralli.sftpserver.core.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ralli.sftpserver.core.dao.SSHPublicKeyDao;
import de.ralli.sftpserver.core.entities.SSHPublicKey;

@Service
public class SSHPublicKeyDaoImpl implements SSHPublicKeyDao {
    private static final Logger logger = LoggerFactory.getLogger(SSHPublicKeyDaoImpl.class);
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public SSHPublicKey findByFingerPrint(String fingerPrint) {
        final String hql = "from SSHPublicKey k where k.fingerPrint=:fingerPrint";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setString("fingerPrint", fingerPrint);
        @SuppressWarnings("unchecked")
        List<SSHPublicKey> list = query.list();
        SSHPublicKey result = list.isEmpty() ? null : list.get(0);
        logger.debug("findByFingerPrint({}) = {}", fingerPrint, result);
        return result;
    }

    @Override
    public void insert(SSHPublicKey publicKey) {
        logger.debug("insert({})", publicKey);
        sessionFactory.getCurrentSession().save(publicKey);
    }

    @Override
    public void update(SSHPublicKey publicKey) {
        logger.debug("update({})", publicKey);
        sessionFactory.getCurrentSession().update(publicKey);
    }

    @Override
    public void delete(SSHPublicKey publicKey) {
        logger.debug("delete({})", publicKey);
        sessionFactory.getCurrentSession().delete(publicKey);
    }

}
