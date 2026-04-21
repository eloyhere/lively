package pers.eloyhere.lively.entity.consumer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import pers.eloyhere.lively.entity.BaseEntity;

import java.util.LinkedHashSet;

@Entity
@Table(name = "route")
public class Route extends BaseEntity {

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "path", unique = true)
    private String path;

    @Column(name = "full", unique = true)
    private String full;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Route parent;

    @OneToMany(mappedBy = "parent")
    private LinkedHashSet<Role> children = new LinkedHashSet<>();

    public Route() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }
}