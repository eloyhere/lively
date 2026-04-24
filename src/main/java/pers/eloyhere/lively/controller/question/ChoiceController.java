package pers.eloyhere.lively.controller.question;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.question.Choice;
import pers.eloyhere.lively.repository.question.ChoiceRepository;
import pers.eloyhere.lively.service.question.ChoiceService;

@RestController("choiceController")
@RequestMapping("/choice")
class ChoiceController extends BaseController<Choice, ChoiceRepository, ChoiceService> {

    public ChoiceController(ChoiceService service) {
        super(service);
    }
}
