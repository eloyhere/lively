package pers.eloyhere.lively.entity.consumer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import pers.eloyhere.lively.entity.BaseEntity;

import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.TreeMap;

@Entity
@Table(name = "token")
public class Token extends BaseEntity {

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "series", nullable = false, unique = true)
    private String series;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.MERGE}, optional = false)
    @JoinColumn(nullable = false)
    private Consumer consumer;

    @Column(name = "last", nullable = false)
    private LocalDateTime last;

    public LocalDateTime getLast() {
        return last;
    }

    @JsonSetter("last")
    public void setLast(LocalDateTime last) {
        this.last = last;
    }

    public void setLast(Date last) {
        this.last = LocalDateTime.ofInstant(last.toInstant(), ZoneOffset.ofHours(8));
    }

    public Token() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        if(Objects.nonNull(consumer)){
            this.consumer = consumer;
            consumer.getTokens().add(this);
        }
    }

    @Override
    public TreeMap<String, Object> properties() {
        TreeMap<String, Object> map = super.properties();
        map.put("token", this.getToken());
        map.put("series", this.getSeries());
        map.put("last", this.getLast());
        return map;
    }

    public PersistentRememberMeToken toPersistentRememberMeToken() {
        return new PersistentRememberMeToken(
                this.getConsumer().getUsername(),
                this.getSeries(),
                this.getToken(),
                Date.from(this.getLast().toInstant(ZoneOffset.ofHours(8)))
        );
    }

    public static Token fromPersistentRememberMeToken(PersistentRememberMeToken token, Consumer consumer) {
        if(Objects.nonNull(token) && Objects.nonNull(consumer)){
            if(Objects.equals(token.getUsername(), consumer.getUsername())){
                Token entity = new Token();
                entity.setConsumer(consumer);
                entity.setToken(token.getTokenValue());
                entity.setSeries(token.getSeries());
                entity.setLast(LocalDateTime.from(token.getDate().toInstant().atOffset(ZoneOffset.ofHours(8))));
                return entity;
            }
        }
        throw new IllegalArgumentException("Invalid token or consumer");
    }
}