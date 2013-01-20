package de.ralli.sftpserver.core.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ralli.sftpserver.core.dao.PartnerKeyDao;
import de.ralli.sftpserver.core.entities.PartnerKey;

@Service
public class PartnerKeyDaoImpl implements PartnerKeyDao {
    private static final Logger logger = LoggerFactory.getLogger(PartnerKeyDaoImpl.class);
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public PartnerKey findByLoginAndFingerPrint(String login, String fingerPrint) {
        final String hql = "from PartnerKey pk join fetch pk.partner join fetch pk.publicKey where pk.login=:login and pk.fingerPrint=:fingerPrint";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setString("login", login);
        query.setString("fingerPrint", fingerPrint);
        @SuppressWarnings("unchecked")
        List<PartnerKey> list = query.list();
        PartnerKey result = list.isEmpty() ? null : list.get(0);
        logger.debug("findByLoginAndFingerPrint({}, {}) = {}", new Object[] { login, fingerPrint, result });
        return result;
    }

    @Override
    public void insert(PartnerKey key) {
        logger.debug("insert({})", key);
        sessionFactory.getCurrentSession().save(key);
    }

    @Override
    public void update(PartnerKey key) {
        logger.debug("update({})", key);
        sessionFactory.getCurrentSession().update(key);
    }

    @Override
    public void delete(PartnerKey key) {
        logger.debug("update({})", key);
        sessionFactory.getCurrentSession().delete(key);
    }
}
