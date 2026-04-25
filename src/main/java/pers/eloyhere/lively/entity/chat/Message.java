package pers.eloyhere.lively.entity.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import pers.eloyhere.lively.entity.BaseEntity;
import pers.eloyhere.lively.entity.consumer.Consumer;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeMap;

@Entity
@Table(name = "message")
public class Message extends BaseEntity {

    @Column(name = "content", nullable = false)
    private String content;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @Enumerated
    @Column(name = "role", nullable = false)
    private ChatRole role;

    public Message() {

    }

    public ChatRole getRole() {
        return role;
    }

    public void setRole(ChatRole role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }


    @Override
    public TreeMap<String, Object> properties() {
        TreeMap<String, Object> map = super.properties();
        map.put("content", this.getContent());
        return map;
    }
}