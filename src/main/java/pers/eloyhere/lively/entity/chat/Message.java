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

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private Consumer sender;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "message_seen",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "consumer_id")
    )
    private Set<Consumer> seen = new LinkedHashSet<>();

    public Message() {

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void see(Consumer consumer){
        this.seen.add(consumer);
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Consumer getSender() {
        return sender;
    }

    public void setSender(Consumer sender) {
        this.sender = sender;
    }

    public Set<Consumer> getSeen() {
        return seen;
    }

    public void setSeen(Set<Consumer> seen) {
        this.seen = seen;
    }

    @Override
    public TreeMap<String, Object> properties() {
        TreeMap<String, Object> map = super.properties();
        map.put("content", this.getContent());
        return map;
    }
}