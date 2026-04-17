package pers.eloyhere.lively.entity;

import jakarta.persistence.*;
import pers.eloyhere.lively.entity.consumer.Consumer;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "announcement")
public class Announcement extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "announcement_consumer",
            joinColumns = @JoinColumn(name = "announcement_id"),
            inverseJoinColumns = @JoinColumn(name = "consumer_id")
    )
    private Set<Consumer> seen = new LinkedHashSet<>();

    public Announcement() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void add(Consumer consumer){
        this.seen.add(consumer);
    }

    public void remove(Consumer consumer){
        this.seen.remove(consumer);
    }

    public Set<Consumer> getSeen() {
        return seen;
    }

    public void setSeen(Set<Consumer> seen) {
        this.seen = seen;
    }
}