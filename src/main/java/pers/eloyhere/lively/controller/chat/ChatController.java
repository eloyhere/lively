package pers.eloyhere.lively.controller.chat;

import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import pers.eloyhere.lively.annotation.Authenticated;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.chat.Chat;
import pers.eloyhere.lively.entity.consumer.Consumer;
import pers.eloyhere.lively.repository.chat.ChatRepository;
import pers.eloyhere.lively.service.chat.ChatService;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.*;

@RestController("chatController")
@RequestMapping("/chat")
class ChatController extends BaseController<Chat, ChatRepository, ChatService> {

    public ChatController(ChatService service) {
        super(service);
    }

    @Authenticated
    @GetMapping("myChats")
    public ResponseEntity<List<Chat>> myChats(){
        return ResponseEntity.ok(this.service.myChats());
    }

    @Authenticated
    @PutMapping("create")
    public ResponseEntity<Chat> create(){
        return this.service.create().map(ResponseEntity::ok).orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @Authenticated
    @PostMapping("message")
    public ResponseEntity<StreamingResponseBody> message(ArrayList<String> content) {
        Map<String, Object> requestBody = Map.of(
                "model", "deepseek-chat",
                "messages", List.of(Map.of("role", "system", "content", "现在你是一名专业的中医药顾问，你只能回答中医药领域相关的问题，其它问题你就回答还没有学会。"), Map.of("role", "user", "content", content)),
                "stream", true
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("sk-bd31f366ccee4aa4bb4e317bd3e1f3ff");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        StreamingResponseBody streamBody = outputStream -> {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Resource> responseEntity = restTemplate.exchange(
                    "https://api.deepseek.com/v1/chat/completions",
                    HttpMethod.POST,
                    entity,
                    Resource.class
            );
            try (InputStream is = responseEntity.getBody().getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line + "\n");
                    writer.flush();
                }
            } catch (Exception e) {
                String errorLine = "event: error\ndata: " + e.getMessage() + "\n\n";
                outputStream.write(errorLine.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }
        };

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(streamBody);
    }
}
