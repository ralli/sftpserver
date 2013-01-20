package de.ralli.sftpserver.core.entities;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;

@Table(name = "partner")
@Entity
public class Partner {
    @Id
    private String login;
    @NotNull
    @Size(max = 80)
    private String host;
    @NotNull
    @Min(value = 0, message = "Port must be greater than zero")
    @Max(value = 65535, message = "Port must be less than 65535")
    private int port;

    @OneToMany
    private Set<PartnerKey> partnerKeys;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Set<PartnerKey> getPartnerKeys() {
        return partnerKeys;
    }

    public void setPartnerKeys(Set<PartnerKey> partnerKeys) {
        this.partnerKeys = partnerKeys;
    }

    @Override
    public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        toStringBuilder.append("login", login).append("host", host).append("port", port);
        return toStringBuilder.toString();
    }
}
