package de.ralli.sftpserver.core.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "partner_key")
public class PartnerKey implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "login")
    private String login;
    @Id
    @Column(name = "fingerprint")
    private String fingerPrint;

    @ManyToOne
    @JoinColumn(name = "login", insertable = false, updatable = false)
    private Partner partner;

    @ManyToOne
    @JoinColumn(name = "fingerprint", insertable = false, updatable = false)
    private SSHPublicKey publicKey;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFingerPrint() {
        return fingerPrint;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public SSHPublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(SSHPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        toStringBuilder.append("login", login).append("fingerPrint", fingerPrint);
        return toStringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PartnerKey))
            return false;

        PartnerKey other = (PartnerKey) obj;
        if (login == null && other.login != null)
            return false;

        if (fingerPrint == null && other.fingerPrint != null)
            return false;

        return login.equals(other.login) && fingerPrint.equals(other.fingerPrint);
    }

    @Override
    public int hashCode() {
        int result = 0;
        if (login != null)
            result ^= login.hashCode();
        if (fingerPrint != null)
            result ^= fingerPrint.hashCode();
        return result;
    }

}
