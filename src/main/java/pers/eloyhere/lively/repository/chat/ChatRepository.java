package pers.eloyhere.lively.repository.chat;

import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.chat.Chat;
import pers.eloyhere.lively.repository.BaseRepository;

@Repository("chatRepository")
public interface ChatRepository extends BaseRepository<Chat> {
}