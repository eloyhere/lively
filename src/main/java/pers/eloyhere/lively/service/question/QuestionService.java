package pers.eloyhere.lively.service.question;

import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.question.Question;
import pers.eloyhere.lively.repository.question.QuestionRepository;
import pers.eloyhere.lively.service.BaseService;

@Service("questionService")
public class QuestionService extends BaseService<Question, QuestionRepository> {

    public QuestionService(QuestionRepository repository) {
        super(repository);
    }
}
