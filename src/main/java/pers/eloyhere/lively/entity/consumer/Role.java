package pers.eloyhere.lively.entity.consumer;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import pers.eloyhere.lively.entity.BaseEntity;

import java.util.*;

@Entity
@Table(name = "role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "role")
public class Role extends BaseEntity implements GrantedAuthority, GrantedAuthoritiesContainer {

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_authority",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "role")
    private Set<Authority> authorities = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_menus",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id")
    )
    @OrderBy("spawn ASC")
    private LinkedHashSet<Menu> menus = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_routes",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "route_id")
    )
    private LinkedHashSet<Route> routes = new LinkedHashSet<>();

    public Role() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public @Nullable String getAuthority() {
        return this.name;
    }

    public void add(Authority authority){
        this.authorities.add(authority);
    }

    public void remove(Authority authority){
        this.authorities.remove(authority);
    }

    @Nonnull
    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities() {
        if(Objects.isNull(this.authorities)){
            return List.of();
        }
        return Collections.unmodifiableSet(this.authorities);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}