package pers.eloyhere.lively.repository.question;

import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.question.Answer;
import pers.eloyhere.lively.repository.BaseRepository;

@Repository("answerRepository")
public interface AnswerRepository extends BaseRepository<Answer> {
}