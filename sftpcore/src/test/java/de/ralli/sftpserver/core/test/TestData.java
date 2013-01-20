package de.ralli.sftpserver.core.test;

import org.springframework.stereotype.Service;

import de.ralli.sftpserver.core.entities.Partner;
import de.ralli.sftpserver.core.entities.SSHPublicKey;

@Service
public class TestData {
    public Partner createTestPartner() {
        Partner partner = new Partner();
        partner.setLogin("testlogin");
        partner.setHost("localhost");
        partner.setPort(22);
        return partner;
    }

    public SSHPublicKey createPublicKey(String fingerPrint) {
        SSHPublicKey result = new SSHPublicKey();
        result.setFingerPrint(fingerPrint);
        result.setKeyData("testdata");
        return result;
    }
}
