package pers.eloyhere.lively.repository.chat;

import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.chat.Message;
import pers.eloyhere.lively.repository.BaseRepository;

@Repository("messageRepository")
public interface MessageRepository extends BaseRepository<Message> {
}