package pers.eloyhere.lively.controller.question;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.question.Question;
import pers.eloyhere.lively.repository.question.QuestionRepository;
import pers.eloyhere.lively.service.question.QuestionService;

@RestController("questionController")
@RequestMapping("/question")
class QuestionController extends BaseController<Question, QuestionRepository, QuestionService> {

    public QuestionController(QuestionService service) {
        super(service);
    }
}
