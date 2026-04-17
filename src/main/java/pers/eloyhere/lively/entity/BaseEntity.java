package pers.eloyhere.lively.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
public class BaseEntity implements Persistable<UUID>, Comparable<BaseEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    protected UUID id;

    @NotNull(message = "Lock could not be null.")
    @Column(name = "`lock`", nullable = false)
    protected LocalDateTime lock;

    @NotNull(message = "Ban could not be null.")
    @Column(name = "ban", nullable = false)
    protected LocalDateTime ban;

    @NotNull(message = "Spawn could not be null.")
    @Column(name = "spawn", nullable = false)
    protected LocalDateTime spawn;

    @NotNull(message = "Edit could not be null.")
    @Column(name = "edit", nullable = false)
    protected LocalDateTime edit;

    @Version
    @Column(name = "version")
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long version;

    @Transient
    @JsonIgnore
    protected final ZoneId zone = ZoneId.of("Asia/Shanghai");

    public BaseEntity() {

    }

    @Override
    public int compareTo(@NonNull BaseEntity o) {
        if(Objects.isNull(this.getId())){
            return Objects.isNull(o.getId()) ? 0 : -1;
        }
        if(Objects.isNull(o.getId())){
            return Objects.isNull(this.getId()) ? 0 : 1;
        }
        return this.getId().compareTo(o.getId());
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now(zone);
        this.setEdit(now);
        this.setSpawn(now);
        this.setLock(now.minusDays(7L));
        this.setBan(now.minusDays(7L));
    }

    @Override
    public @Nullable UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return Objects.isNull(this.getId());
    }

    @PreUpdate
    public void preUpdate() {
        LocalDateTime now = LocalDateTime.now(zone);
        this.setEdit(now);
    }

    public ZoneId getZone() {
        return zone;
    }

    public Boolean locked(){
        if(Objects.isNull(this.lock)){
            return false;
        }
        LocalDateTime now = LocalDateTime.now(zone);
        return this.lock.isAfter(now);
    }

    public Boolean banned(){
        if(Objects.isNull(this.ban)){
            return false;
        }
        LocalDateTime now = LocalDateTime.now(zone);
        return this.ban.isAfter(now);
    }

    public LocalDateTime getLock() {
        return lock;
    }

    public void setLock(LocalDateTime lock) {
        this.lock = lock;
    }

    public LocalDateTime getBan() {
        return ban;
    }

    public void setBan(LocalDateTime ban) {
        this.ban = ban;
    }

    public LocalDateTime getSpawn() {
        return spawn;
    }

    public void setSpawn(LocalDateTime spawn) {
        this.spawn = spawn;
    }

    public LocalDateTime getEdit() {
        return edit;
    }

    public void setEdit(LocalDateTime edit) {
        this.edit = edit;
    }

    public void lock(){
        LocalDateTime now = LocalDateTime.now(zone);
        this.setLock(now.plusDays(1L));
    }

    public void lock(LocalDateTime end){
        if(Objects.nonNull(end)){
            this.setLock(end);
        }
    }

    public void ban(){
        LocalDateTime now = LocalDateTime.now(zone);
        this.setBan(now.plusDays(1L));
    }

    public void ban(LocalDateTime end){
        if(Objects.nonNull(end)){
            this.setBan(end);
        }
    }

    public Long getVersion() {
        return version;
    }

    @Override
    public final boolean equals(Object o) {
        if(this == o){
            return true;
        }
        if(o instanceof BaseEntity other){
            return Objects.equals(this.getId(), other.getId());
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.getId(), this.getBan(), this.getEdit(), this.getLock(), this.getVersion());
    }
}