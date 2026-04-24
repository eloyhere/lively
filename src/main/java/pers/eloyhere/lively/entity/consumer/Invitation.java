package pers.eloyhere.lively.entity.consumer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pers.eloyhere.lively.entity.BaseEntity;

import java.util.TreeMap;

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

    @Override
    public TreeMap<String, Object> properties() {
        TreeMap<String, Object> map = super.properties();
        map.put("code", this.getCode());
        return map;
    }
}