package pers.eloyhere.lively.entity.consumer;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import pers.eloyhere.lively.entity.BaseEntity;

@Entity
@Table(name = "authority")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "authority")
@NaturalIdCache
public class Authority extends BaseEntity implements GrantedAuthority {

    @Column(name = "authority", nullable = false, unique = true)
    private String authority;

    public Authority() {
    }

    public Authority(String authority) {
        this.authority = authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public @Nullable String getAuthority() {
        return this.authority;
    }
}