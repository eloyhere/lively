package pers.eloyhere.lively.controller.tcm;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.tcm.TcmQuestion;
import pers.eloyhere.lively.repository.tcm.TcmQuestionRepository;
import pers.eloyhere.lively.service.tcm.TcmQuestionService;

@RestController("tcmQuestionController")
@RequestMapping("/tcm/question")
public class TcmQuestionController extends BaseController<TcmQuestion, TcmQuestionRepository, TcmQuestionService> {

    public TcmQuestionController(TcmQuestionService service) {
        super(service);
    }
}
