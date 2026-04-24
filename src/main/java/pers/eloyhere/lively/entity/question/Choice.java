package pers.eloyhere.lively.entity.question;

import jakarta.persistence.*;
import pers.eloyhere.lively.entity.BaseEntity;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeMap;

@Entity
@Table(name = "choice")
public class Choice extends BaseEntity {
    @Column(name = "question", nullable = false, unique = true)
    private String question;

    @OneToMany(mappedBy = "choice", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Answer> answers = new LinkedHashSet<>();

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public TreeMap<String, Object> properties() {
        TreeMap<String, Object> map = super.properties();
        map.put("question", this.getQuestion());
        return map;
    }
}