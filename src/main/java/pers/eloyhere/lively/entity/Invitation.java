package pers.eloyhere.lively.entity;

import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "invitation")
public class Invitation extends BaseEntity {

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    public Invitation() {
    }

    public String getCode() {
        return code;
    }

    @JsonSetter
    public void setCode(String code) {
        this.code = code;
    }

    public void setCode(Long code){
        this.code = Long.toHexString(code);
    }
}