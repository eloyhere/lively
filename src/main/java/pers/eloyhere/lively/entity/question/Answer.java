package pers.eloyhere.lively.entity.question;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import pers.eloyhere.lively.entity.BaseEntity;

import java.util.TreeMap;

@Entity
@Table(name = "answer")
public class Answer extends BaseEntity {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "choice_id")
    private Choice choice;

    @Column(name = "proper", nullable = false)
    private Boolean proper = false;

    @Column(name = "content", nullable = false)
    private String content;

    public Answer() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getProper() {
        return proper;
    }

    public void setProper(Boolean proper) {
        this.proper = proper;
    }

    public Choice getChoice() {
        return choice;
    }

    public void setChoice(Choice choice) {
        this.choice = choice;
    }

    @Override
    public TreeMap<String, Object> properties() {
        TreeMap<String, Object> map = super.properties();
        map.put("proper", this.getProper());
        return map;
    }
}