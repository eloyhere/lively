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

    @Transactional
    public Chat insert(Authentication authentication, String name, String description) {
        Chat chat = new Chat();
        Consumer consumer = (Consumer) authentication.getPrincipal();
        chat.setConsumer(consumer);
        chat.setChatName(name);
        chat.setDescription(description);
        return repository.saveAndFlush(chat);
    }


    private String extract(String sseLine) {
        try {
            if (sseLine.startsWith("data: ")) {
                String json = sseLine.substring(6).trim();
                if ("[DONE]".equals(json)) {
                    return null;
                }
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(json);
                JsonNode choices = node.path("choices");
                if (choices.isArray() && !choices.isEmpty()) {
                    JsonNode delta = choices.get(0).path("delta").path("content");
                    return delta.asText(null);
                }
            }
        } catch (Exception e) {

        }
        return null;
    }

    public void stream(Chat chat, String userMessage, WebSocketSession session) {
        Map<String, Object> body = new HashMap<>();
        body.put("model", "deepseek-chat");
        body.put("messages", List.of(Map.of("role", "user", "content", userMessage)));
        body.put("stream", true);

        StringBuffer buffer = new StringBuffer();
        client.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .subscribe(
                        (sse) -> {
                            try{
                                String token = extract(sse);
                                if (Objects.nonNull(token) && session.isOpen()) {
                                    buffer.append(token);
                                    TextMessage message = new TextMessage(token);
                                    session.sendMessage(message);
                                }
                            }catch (Exception ignored){

                            }
                        },
                        (error) -> {
                            try{
                                TextMessage message = new TextMessage(error.getMessage());
                                session.sendMessage(message);
                            }catch (Exception ignored){

                            }
                        },
                        () -> {
                            try{
                                Message message = new Message();
                                message.setRole(ChatRole.Assistant);
                                message.setContent(buffer.toString());
                                chat.add(message);
                                session.sendMessage(new TextMessage("done"));
                            }catch (Exception ignored){

                            }
                        }
                );
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
