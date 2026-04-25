package pers.eloyhere.lively.entity.chat;

import jakarta.persistence.*;
import pers.eloyhere.lively.entity.BaseEntity;
import pers.eloyhere.lively.entity.consumer.Consumer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

@Entity
@Table(name = "chat")
public class Chat extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description", length = 500)
    private String description;



    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("spawn ASC")
    private Set<Message> messages = new LinkedHashSet<>();

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false, orphanRemoval = true)
    @JoinColumn(name = "consumer_id", nullable = false)
    private Consumer consumer;

    public Chat() {
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public void add(Message message){
        if(Objects.nonNull(message)){
            this.messages.add(message);
            message.setChat(this);
        }
    }

    public void remove(Message message){
        if(Objects.nonNull(message)){
            this.messages.remove(message);
        }
    }

    public void send(Consumer consumer, Message message){
        this.messages.add(message);
        message.setChat(this);
    }

    public void withdraw(Message message){
        if(Objects.nonNull(message)){
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
            message.setLock(now.plusYears(100));
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setChatName(String name) {
        this.name = name;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }
    @Override
    public TreeMap<String, Object> properties() {
        TreeMap<String, Object> map = super.properties();
        map.put("name", this.getName());
        map.put("description", this.getDescription());
        return map;
    }
}