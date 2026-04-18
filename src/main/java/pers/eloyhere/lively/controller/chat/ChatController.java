package pers.eloyhere.lively.controller.chat;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.chat.Chat;
import pers.eloyhere.lively.repository.chat.ChatRepository;
import pers.eloyhere.lively.service.chat.ChatService;

@RestController("chatController")
@RequestMapping("/chat")
class ChatController extends BaseController<Chat, ChatRepository, ChatService> {

    public ChatController(ChatService service) {
        super(service);
    }
}
