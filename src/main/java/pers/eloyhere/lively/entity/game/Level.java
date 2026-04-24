package pers.eloyhere.lively.entity.game;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pers.eloyhere.lively.entity.BaseEntity;

import java.util.TreeMap;

@Entity
@Table(name = "level")
public class Level extends BaseEntity {
    @Column(name = "icon", nullable = false)
    private String icon;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "order", nullable = false)
    private Integer order;

    public Level() {
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public TreeMap<String, Object> properties() {
        TreeMap<String, Object> map = super.properties();
        map.put("icon", this.getIcon());
        map.put("name", this.getName());
        map.put("order", this.getOrder());
        return map;
    }
}