package de.ralli.sftpserver.core.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ralli.sftpserver.core.dao.PartnerDao;
import de.ralli.sftpserver.core.entities.Partner;

@Service
public class PartnerDaoImpl implements PartnerDao {
    @Autowired
    private SessionFactory sessionFactory;
    private static final Logger logger = LoggerFactory.getLogger(PartnerDaoImpl.class);

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Partner findByLogin(String login) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Partner p where p.login=:login");
        query.setString("login", login);
        @SuppressWarnings("unchecked")
        List<Partner> list = query.list();
        Partner result = list.isEmpty() ? null : list.get(0);
        logger.debug("findByLogin({}) = {}", login, result);
        return result;
    }

    @Override
    public void insert(Partner partner) {
        logger.debug("insert({})", partner);
        sessionFactory.getCurrentSession().save(partner);
    }

    @Override
    public void update(Partner partner) {
        logger.debug("update({})", partner);
        sessionFactory.getCurrentSession().update(partner);
    }

    @Override
    public void delete(Partner partner) {
        logger.debug("delete({})", partner);
        getSessionFactory().getCurrentSession().delete(partner);
    }
}
