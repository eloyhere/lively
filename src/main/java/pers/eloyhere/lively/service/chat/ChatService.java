package pers.eloyhere.lively.service.chat;

import jakarta.transaction.Transactional;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import pers.eloyhere.lively.entity.chat.Chat;
import pers.eloyhere.lively.entity.chat.ChatRole;
import pers.eloyhere.lively.entity.chat.Message;
import pers.eloyhere.lively.entity.consumer.Consumer;
import pers.eloyhere.lively.repository.chat.ChatRepository;
import pers.eloyhere.lively.repository.chat.MessageRepository;
import pers.eloyhere.lively.repository.consumer.ConsumerRepository;
import pers.eloyhere.lively.service.BaseService;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectReader;
import tools.jackson.databind.ObjectWriter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service("chatService")
public class ChatService extends BaseService<Chat, ChatRepository> {

    private final WebClient client;

    private final ConsumerRepository consumerRepository;

    private final MessageRepository messageRepository;

    private static final String url = "https://api.deepseek.com";

    private static final String bearer = "sk-bd31f366ccee4aa4bb4e317bd3e1f3ff";

    public ChatService(ChatRepository repository, ConsumerRepository consumerRepository, MessageRepository messageRepository) {
        super(repository);
        this.consumerRepository = consumerRepository;
        this.messageRepository = messageRepository;
        this.client = WebClient.builder().baseUrl("https://api.deepseek.com").defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer "+bearer).build();
    }

    public Optional<Chat> create(){
        Chat chat = new Chat();

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if(Objects.isNull(authentication) || Objects.isNull(authentication.getPrincipal())){
            return Optional.empty();
        }
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.ss");
        chat.setChatName(formatter.format(now));
        chat.setDescription(formatter.format(now));
        chat.setConsumer((Consumer) authentication.getPrincipal());
        return Optional.of(this.repository.saveAndFlush(chat));
    }



    public List<Chat> myChats(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(Objects.isNull(authentication)){
            return List.of();
        }
        Consumer consumer = (Consumer) authentication.getPrincipal();
        return this.repository.findByConsumer_Id(consumer.getId());
    }
}
