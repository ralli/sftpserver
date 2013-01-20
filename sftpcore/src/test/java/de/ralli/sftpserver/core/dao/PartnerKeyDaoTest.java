package de.ralli.sftpserver.core.dao;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.ralli.sftpserver.core.entities.Partner;
import de.ralli.sftpserver.core.entities.PartnerKey;
import de.ralli.sftpserver.core.entities.SSHPublicKey;
import de.ralli.sftpserver.core.test.SpringTestConfig;
import de.ralli.sftpserver.core.test.TestData;

@Transactional
@RunWith(org.springframework.test.context.junit4.SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringTestConfig.class })
public class PartnerKeyDaoTest {
    @Autowired
    private PartnerKeyDao partnerKeyDao;
    @Autowired
    private PartnerDao partnerDao;
    @Autowired
    private SSHPublicKeyDao sshPublicKeyDao;
    @Autowired
    private TestData testData;
    private static final String fingerPrint = "hase";
    private Partner partner;
    private SSHPublicKey publicKey;

    private void insertPartnerAndKey() {
        partner = testData.createTestPartner();
        partnerDao.insert(partner);
        publicKey = testData.createPublicKey(fingerPrint);
        sshPublicKeyDao.insert(publicKey);
    }

    @Test
    public void testInsertDelete() {
        insertPartnerAndKey();
        PartnerKey partnerKey = new PartnerKey();
        partnerKey.setLogin(partner.getLogin());
        partnerKey.setFingerPrint(publicKey.getFingerPrint());
        partnerKeyDao.insert(partnerKey);
        partnerKey = partnerKeyDao.findByLoginAndFingerPrint(partner.getLogin(), publicKey.getFingerPrint());
        Assert.assertNotNull(partnerKey);
        partnerKeyDao.delete(partnerKey);
        partnerKey = partnerKeyDao.findByLoginAndFingerPrint(partner.getLogin(), publicKey.getFingerPrint());
        Assert.assertNull(partnerKey);
    }
}
