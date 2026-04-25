package pers.eloyhere.lively.entity.log;

import jakarta.persistence.*;
import pers.eloyhere.lively.entity.BaseEntity;
import pers.eloyhere.lively.entity.consumer.Consumer;

import java.util.TreeMap;

@Entity
@Table(name = "operation")
public class Operation extends BaseEntity {
    @OneToOne(cascade = {CascadeType.REFRESH}, orphanRemoval = true)
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;

    @Column(name = "url", nullable = false)
    private String url;

    public Operation() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public TreeMap<String, Object> properties() {
        TreeMap<String, Object> map = super.properties();
        map.put("url", this.getUrl());
        return map;
    }
}