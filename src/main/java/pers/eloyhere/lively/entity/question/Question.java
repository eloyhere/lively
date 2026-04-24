package pers.eloyhere.lively.entity.question;

import jakarta.persistence.*;
import pers.eloyhere.lively.entity.BaseEntity;

import java.util.TreeMap;

@Entity
@Table(name = "question")
public class Question extends BaseEntity {

    @Column(name = "question", nullable = false, unique = true)
    private String question;

    @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;

    public Question() {
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
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