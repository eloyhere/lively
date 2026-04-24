package pers.eloyhere.lively.service.question;

import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.question.Choice;
import pers.eloyhere.lively.repository.question.ChoiceRepository;
import pers.eloyhere.lively.service.BaseService;

@Service("choiceService")
public class ChoiceService extends BaseService<Choice, ChoiceRepository> {

    public ChoiceService(ChoiceRepository repository) {
        super(repository);
    }
}
