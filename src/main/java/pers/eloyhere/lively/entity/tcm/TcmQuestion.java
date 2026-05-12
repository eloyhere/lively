package pers.eloyhere.lively.entity.tcm;

import jakarta.persistence.*;
import pers.eloyhere.lively.entity.BaseEntity;

import java.util.TreeMap;

@Entity
@Table(name = "tcm_question")
public class TcmQuestion extends BaseEntity {

    @Column(name = "subject", nullable = false, length = 50)
    private String subject;

    @Column(name = "type", nullable = false, length = 20)
    private String type;

    @Column(name = "question", nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(name = "options", nullable = false, columnDefinition = "TEXT")
    private String options;

    @Column(name = "answer", nullable = false, columnDefinition = "TEXT")
    private String answer;

    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "key_point", length = 200)
    private String keyPoint;

    @Column(name = "difficulty", nullable = false)
    private Integer difficulty;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getKeyPoint() {
        return keyPoint;
    }

    public void setKeyPoint(String keyPoint) {
        this.keyPoint = keyPoint;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public TreeMap<String, Object> properties() {
        TreeMap<String, Object> map = super.properties();
        map.put("subject", this.getSubject());
        map.put("type", this.getType());
        map.put("question", this.getQuestion());
        map.put("options", this.getOptions());
        map.put("answer", this.getAnswer());
        map.put("explanation", this.getExplanation());
        map.put("keyPoint", this.getKeyPoint());
        map.put("difficulty", this.getDifficulty());
        return map;
    }
}
