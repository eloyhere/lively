package pers.eloyhere.lively.repository.tcm;

import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.tcm.TcmQuestion;
import pers.eloyhere.lively.repository.BaseRepository;

@Repository("tcmQuestionRepository")
public interface TcmQuestionRepository extends BaseRepository<TcmQuestion> {
}
