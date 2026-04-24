package pers.eloyhere.lively.controller.log;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.log.Visit;
import pers.eloyhere.lively.repository.log.VisitRepository;
import pers.eloyhere.lively.service.log.VisitService;

@RestController("visitController")
@RequestMapping("/visit")
class VisitController extends BaseController<Visit, VisitRepository, VisitService> {

    public VisitController(VisitService service) {
        super(service);
    }
}
