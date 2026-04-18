package pers.eloyhere.lively.entity.chat;

import jakarta.persistence.*;
import pers.eloyhere.lively.entity.BaseEntity;
import pers.eloyhere.lively.entity.consumer.Consumer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "chat")
public class Chat extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "chat_admins",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "consumer_id")
    )
    private Set<Consumer> administrators = new LinkedHashSet<>();

    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("spawn ASC")
    private Set<Message> messages = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "chat_members",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    @OrderBy("username ASC")
    private Set<Consumer> members = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner")
    private Consumer owner;

    public Chat() {
    }

    public void add(Consumer consumer){
        this.members.add(consumer);
    }

    public void remove(Consumer consumer){
        this.members.remove(consumer);
    }

    public void send(Consumer consumer, Message message){
        message.setSender(consumer);
    }

    public void withdraw(Consumer consumer, Message message){
        if(Objects.equals(consumer, message.getSender())){
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

    public Set<Consumer> getAdministrators() {
        return administrators;
    }

    public void setAdministrators(Set<Consumer> administrators) {
        this.administrators = administrators;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Set<Consumer> getMembers() {
        return members;
    }

    public void setMembers(Set<Consumer> members) {
        this.members = members;
    }

    public Consumer getOwner() {
        return owner;
    }

    public void setOwner(Consumer owner) {
        this.owner = owner;
    }
}