package pers.eloyhere.lively.repository.question;

import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.question.Question;
import pers.eloyhere.lively.repository.BaseRepository;

@Repository("questionRepository")
public interface QuestionRepository extends BaseRepository<Question> {
}