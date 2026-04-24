package pers.eloyhere.lively.service.log;

import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.log.Visit;
import pers.eloyhere.lively.repository.log.VisitRepository;
import pers.eloyhere.lively.service.BaseService;

@Service("visitService")
public class VisitService extends BaseService<Visit, VisitRepository> {

    public VisitService(VisitRepository repository) {
        super(repository);
    }
}
