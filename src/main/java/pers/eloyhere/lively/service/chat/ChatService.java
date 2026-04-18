package pers.eloyhere.lively.service.chat;

import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.chat.Chat;
import pers.eloyhere.lively.repository.chat.ChatRepository;
import pers.eloyhere.lively.service.BaseService;

@Service("chatService")
public class ChatService extends BaseService<Chat, ChatRepository> {
    public ChatService(ChatRepository repository) {
        super(repository);
    }
}
