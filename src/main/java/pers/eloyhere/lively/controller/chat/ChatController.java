package pers.eloyhere.lively.controller.chat;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.chat.Chat;
import pers.eloyhere.lively.entity.consumer.Consumer;
import pers.eloyhere.lively.repository.chat.ChatRepository;
import pers.eloyhere.lively.service.chat.ChatService;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController("chatController")
@RequestMapping("/chat")
class ChatController extends BaseController<Chat, ChatRepository, ChatService> {

    public ChatController(ChatService service) {
        super(service);
    }

    @GetMapping("myChats")
    public ResponseEntity<List<Chat>> myChats(){
        return ResponseEntity.ok(this.service.myChats());
    }
}
