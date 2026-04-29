package pers.eloyhere.lively.controller.question;

import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import pers.eloyhere.lively.annotation.Authenticated;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.question.Question;
import pers.eloyhere.lively.repository.question.QuestionRepository;
import pers.eloyhere.lively.service.question.QuestionService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController("questionController")
@RequestMapping("/question")
class QuestionController extends BaseController<Question, QuestionRepository, QuestionService> {

    public QuestionController(QuestionService service) {
        super(service);
    }

    @Authenticated
    @PostMapping("help")
    public ResponseEntity<StreamingResponseBody> help(ArrayList<String> content){
        Map<String, Object> requestBody = Map.of(
                "model", "deepseek-chat",
                "messages", List.of(Map.of("role", "system", "content", "现在你是一名专业的学习分析指导员，你只能回答中医药领域学习相关的问题，其它问题你就回答还没有学会。用户给出答题记录后，你要根据答题记录分析用户知识领域薄弱的地方，给用户制定计划。"), Map.of("role", "user", "content", content)),
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
