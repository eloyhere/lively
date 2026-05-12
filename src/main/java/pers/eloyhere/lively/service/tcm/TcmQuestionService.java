package pers.eloyhere.lively.service.tcm;

import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.tcm.TcmQuestion;
import pers.eloyhere.lively.repository.tcm.TcmQuestionRepository;
import pers.eloyhere.lively.service.BaseService;

@Service("tcmQuestionService")
public class TcmQuestionService extends BaseService<TcmQuestion, TcmQuestionRepository> {

    public TcmQuestionService(TcmQuestionRepository repository) {
        super(repository);
    }
}
