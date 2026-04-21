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


}