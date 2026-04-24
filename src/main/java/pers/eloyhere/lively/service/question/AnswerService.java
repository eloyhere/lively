package pers.eloyhere.lively.service.question;

import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.question.Answer;
import pers.eloyhere.lively.repository.question.AnswerRepository;
import pers.eloyhere.lively.service.BaseService;

@Service("answerService")
public class AnswerService extends BaseService<Answer, AnswerRepository> {

    public AnswerService(AnswerRepository repository) {
        super(repository);
    }
}
