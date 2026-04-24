package pers.eloyhere.lively.entity.consumer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import pers.eloyhere.lively.entity.BaseEntity;

import java.util.TreeMap;

@Entity
@Table(name = "authority")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "authority")
@NaturalIdCache
public class Authority extends BaseEntity implements GrantedAuthority {

    @Column(name = "authority", nullable = false, unique = true)
    private String authority;

    @Column(name = "description")
    private String description;

    public Authority() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public @Nullable String getAuthority() {
        return this.authority;
    }

    @Override
    public TreeMap<String, Object> properties() {
        TreeMap<String, Object> map = super.properties();
        map.put("name", this.getAuthority());
        map.put("description", this.getDescription());
        return map;
    }
}