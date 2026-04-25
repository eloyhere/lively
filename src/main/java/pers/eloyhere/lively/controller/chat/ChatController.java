package pers.eloyhere.lively.controller.chat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.chat.Chat;
import pers.eloyhere.lively.repository.chat.ChatRepository;
import pers.eloyhere.lively.service.chat.ChatService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController("chatController")
@RequestMapping("/chat")
class ChatController extends BaseController<Chat, ChatRepository, ChatService> {

    public ChatController(ChatService service) {
        super(service);
    }

    @PostMapping("chat")
    public ResponseEntity<String> chat(UUID identifier, String content){
        return this.service.chat(identifier, content);
    }

    @PutMapping("create")
    public ResponseEntity<Chat> create(String content){
        Optional<Chat> optional = this.service.create(content);
        return optional.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @GetMapping("chats")
    public ResponseEntity<List<Chat>> chats(){
        return ResponseEntity.ok(this.service.chats());
    }


}
