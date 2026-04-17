package pers.eloyhere.lively.entity.consumer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pers.eloyhere.lively.converter.StringBlobConverter;
import pers.eloyhere.lively.entity.BaseEntity;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "consumer")
public class Consumer extends BaseEntity implements UserDetails {
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @JsonIgnore
    @Convert(converter = StringBlobConverter.class)
    @Column(name = "password", nullable = false)
    @JdbcTypeCode(SqlTypes.BLOB)
    private String password;

    @Lob
    @Column(name = "avatar", nullable = false)
    private String avatar;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "consumer_roles",
            joinColumns = @JoinColumn(name = "consumer_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "consumer", cascade = {CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    private Set<Token> tokens = new LinkedHashSet<>();

    public Consumer() {

    }

    public void add(Role role){
        this.roles.add(role);
    }

    public void remove(Role role){
        this.roles.remove(role);
    }

    @Nonnull
    public Set<Role> getRoles() {
        if(Objects.isNull(this.roles)){
            return Set.of();
        }
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Nullable
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Nonnull
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(Objects.isNull(this.roles)){
            return List.of();
        }
        return this.roles.stream().flatMap((role) -> role.getGrantedAuthorities().stream()).collect(Collectors.toSet());
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Nullable
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Token> getTokens() {
        return tokens;
    }

    public void setTokens(Set<Token> tokens) {
        this.tokens = tokens;
    }
}