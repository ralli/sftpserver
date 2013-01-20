package de.ralli.sftpserver.core.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.ralli.sftpserver.core.dao.impl.SSHPublicKeyDaoImpl;
import de.ralli.sftpserver.core.entities.SSHPublicKey;
import de.ralli.sftpserver.core.test.SpringTestConfig;

@Transactional
@RunWith(org.springframework.test.context.junit4.SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringTestConfig.class })
public class SSHPublicKeyDaoTest {
    @Autowired
    private SSHPublicKeyDaoImpl sshPublicKeyDao;
    private static final String FINGERPRINT = "testfingerprint";

    private SSHPublicKey createPublicKey() {
        SSHPublicKey result = new SSHPublicKey();
        result.setFingerPrint(FINGERPRINT);
        result.setKeyData("testdata");
        return result;
    }

    @Test
    public void testInsertUpdateDelete() {
        SSHPublicKey publicKey = createPublicKey();
        sshPublicKeyDao.insert(publicKey);
        publicKey = sshPublicKeyDao.findByFingerPrint(FINGERPRINT);
        Assert.assertNotNull(publicKey);
        publicKey.setKeyData("horst");
        sshPublicKeyDao.update(publicKey);
        publicKey = sshPublicKeyDao.findByFingerPrint(FINGERPRINT);
        Assert.assertNotNull(publicKey);
        Assert.assertEquals("horst", publicKey.getKeyData());
        sshPublicKeyDao.delete(publicKey);
        publicKey = sshPublicKeyDao.findByFingerPrint(FINGERPRINT);
        Assert.assertNull(publicKey);
    }
}
