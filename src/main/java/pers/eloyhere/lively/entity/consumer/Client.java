package pers.eloyhere.lively.entity.consumer;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pers.eloyhere.lively.entity.BaseEntity;

@Entity
@Table(name = "client")
public class Client extends BaseEntity {

    private String fingerprint;

    public Client() {
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
}