package pers.eloyhere.lively.service.chat;

import jakarta.transaction.Transactional;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pers.eloyhere.lively.entity.chat.Chat;
import pers.eloyhere.lively.entity.chat.ChatRole;
import pers.eloyhere.lively.entity.chat.Message;
import pers.eloyhere.lively.entity.consumer.Consumer;
import pers.eloyhere.lively.repository.chat.ChatRepository;
import pers.eloyhere.lively.service.BaseService;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectReader;
import tools.jackson.databind.ObjectWriter;

import java.util.*;

@Service("chatService")
public class ChatService extends BaseService<Chat, ChatRepository> {

    private final RestTemplate template = new RestTemplate();

    public ChatService(ChatRepository repository) {
        super(repository);
    }

    @Transactional
    public ResponseEntity<String> chat(final UUID identifier, String content){
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerFor(HashMap.class);
        ObjectReader reader = mapper.readerFor(HashMap.class);
        Optional<ResponseEntity<String>> optional = this.repository.findById(identifier).map((chat) -> {
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = context.getAuthentication();
            if(Objects.nonNull(authentication) && authentication.getPrincipal() instanceof Consumer consumer){
                Message message = new Message();
                message.setContent(content);
                message.setRole(ChatRole.User);
                chat.add(message);

                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                httpHeaders.set("Authorization", "Bearer sk-1a78e3cd5a774b6cbf8a2f0a26371c1e");
                HashMap<String, Object> map = new HashMap<>();
                map.put("model", "deepseek-v4-flash");
                map.put("messages", chat);
                map.put("temperature", 0.7);
                HttpEntity<String> request = new HttpEntity<>(writer.writeValueAsString(map), httpHeaders);
                ResponseEntity<String> response = template.exchange("https://api.deepseek.com/chat/completions?response_format=json", HttpMethod.POST, request, String.class);
                System.out.println(reader.readValue(response.getBody())+response.getBody());
                this.repository.saveAndFlush(chat);
                return response;
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        });
        return optional.orElse(ResponseEntity.notFound().build());
    }

    public List<Chat> chats(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if(Objects.nonNull(authentication) && authentication.getPrincipal() instanceof Consumer consumer){
            return this.repository.findByConsumer_Id(consumer.getId());
        }
        return List.of();
    }

    public Optional<Chat> create(String content){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if(Objects.nonNull(authentication) && authentication.getPrincipal() instanceof Consumer consumer){
            Chat chat = new Chat();
            chat.setConsumer(consumer);
            Message message = new Message();
            message.setContent(content);
            message.setRole(ChatRole.User);
            chat.add(message);
            chat.setChatName(content.substring(0, 5).concat("..."));
            chat.setDescription(content);
            return Optional.of(this.repository.saveAndFlush(chat));
        }
        return Optional.empty();
    }
}
