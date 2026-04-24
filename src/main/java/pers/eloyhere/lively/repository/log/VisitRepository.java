package pers.eloyhere.lively.repository.log;

import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.log.Visit;
import pers.eloyhere.lively.repository.BaseRepository;

@Repository("visitRepository")
public interface VisitRepository extends BaseRepository<Visit> {
}