package pers.eloyhere.lively.controller.question;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.question.Answer;
import pers.eloyhere.lively.repository.question.AnswerRepository;
import pers.eloyhere.lively.service.question.AnswerService;

@RestController("answerController")
@RequestMapping("/answer")
class AnswerController extends BaseController<Answer, AnswerRepository, AnswerService> {

    public AnswerController(AnswerService service) {
        super(service);
    }
}
