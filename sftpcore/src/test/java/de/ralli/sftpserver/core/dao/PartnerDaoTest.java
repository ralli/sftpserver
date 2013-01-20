package de.ralli.sftpserver.core.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.ralli.sftpserver.core.entities.Partner;
import de.ralli.sftpserver.core.test.SpringTestConfig;

@Transactional
@RunWith(org.springframework.test.context.junit4.SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringTestConfig.class })
public class PartnerDaoTest {
    @Autowired
    private PartnerDao partnerDao;

    private Partner createTestPartner() {
        Partner partner = new Partner();
        partner.setLogin("testlogin");
        partner.setHost("localhost");
        partner.setPort(22);
        return partner;
    }

    @Test
    public void testFindByLogin() {
        Partner partner = createTestPartner();
        partnerDao.insert(partner);
        partner = partnerDao.findByLogin("testlogin");
        Assert.assertNotNull(partner);
        partner.setHost("google.de");
        partnerDao.update(partner);
        partner = partnerDao.findByLogin("testlogin");
        Assert.assertNotNull(partner);
        Assert.assertEquals("google.de", partner.getHost());
        partnerDao.delete(partner);
        partner = partnerDao.findByLogin("testlogin");
        Assert.assertNull(partner);
    }
}
