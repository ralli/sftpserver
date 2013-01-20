package de.ralli.sftpserver.core.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "publickey")
public class SSHPublicKey {
    @Id
    @Column(name = "fingerprint")
    private String fingerPrint;
    @Lob
    @NotNull
    @Column(name = "keydata")
    private String keyData;

    public String getFingerPrint() {
        return fingerPrint;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public String getKeyData() {
        return keyData;
    }

    public void setKeyData(String keyData) {
        this.keyData = keyData;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
