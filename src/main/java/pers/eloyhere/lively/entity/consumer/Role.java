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

    @JoinTable(
            name = "role_authorities",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "role")
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    private Set<Authority> authorities = new LinkedHashSet<>();

    @JoinTable(
            name = "role_menus",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id")
    )
    @OrderBy("spawn ASC")
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Menu> menus = new LinkedHashSet<>();


    @JoinTable(
            name = "role_routes",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "route_id")
    )
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Route> routes = new LinkedHashSet<>();

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

    public void add(Route route){
        this.routes.add(route);
    }

    public void remove(Route route){
        this.routes.remove(route);
    }

    public void add(Menu menu){
        this.menus.add(menu);
    }

    public void remove(Menu menu){
        this.menus.remove(menu);
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

    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(LinkedHashSet<Menu> menus) {
        this.menus = menus;
    }

    public Set<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(LinkedHashSet<Route> routes) {
        this.routes = routes;
    }
}