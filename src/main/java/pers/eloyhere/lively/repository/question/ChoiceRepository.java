package pers.eloyhere.lively.repository.question;

import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.question.Choice;
import pers.eloyhere.lively.repository.BaseRepository;

@Repository("choiceRepository")
public interface ChoiceRepository extends BaseRepository<Choice> {
}