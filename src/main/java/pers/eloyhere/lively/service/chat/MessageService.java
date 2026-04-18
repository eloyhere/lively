package pers.eloyhere.lively.service.chat;

import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.chat.Message;
import pers.eloyhere.lively.repository.chat.MessageRepository;
import pers.eloyhere.lively.service.BaseService;

@Service("messageService")
public class MessageService extends BaseService<Message, MessageRepository> {
    public MessageService(MessageRepository repository) {
        super(repository);
    }
}
