package pers.eloyhere.lively.entity.question;

import jakarta.persistence.*;
import pers.eloyhere.lively.entity.BaseEntity;

import java.util.TreeMap;

@Entity
@Table(name = "question")
public class Question extends BaseEntity {

    @Column(name = "question", nullable = false, unique = true)
    private String question;

    @Column(name = "answer", nullable = false)
    private String answer;

    public Question() {
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
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