package pers.eloyhere.lively.controller.log;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.log.Operation;
import pers.eloyhere.lively.repository.log.OperationRepository;
import pers.eloyhere.lively.service.log.OperationService;

@RestController("operationController")
@RequestMapping("/operation")
class OperationController extends BaseController<Operation, OperationRepository, OperationService> {

    public OperationController(OperationService service) {
        super(service);
    }
}
