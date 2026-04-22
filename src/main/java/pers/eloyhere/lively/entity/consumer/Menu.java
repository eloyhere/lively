package pers.eloyhere.lively.entity.consumer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import pers.eloyhere.lively.entity.BaseEntity;

import java.util.LinkedHashSet;

@Entity
@Table(name = "menu")
public class Menu extends BaseEntity {

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "path", unique = true)
    private String path;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "parent_id")
    private Menu parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
    private LinkedHashSet<Menu> children = new LinkedHashSet<>();

    public Menu() {
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
}