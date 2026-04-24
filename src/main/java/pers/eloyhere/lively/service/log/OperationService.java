package pers.eloyhere.lively.service.log;

import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.log.Operation;
import pers.eloyhere.lively.repository.log.OperationRepository;
import pers.eloyhere.lively.service.BaseService;

@Service("operationService")
public class OperationService extends BaseService<Operation, OperationRepository> {

    public OperationService(OperationRepository repository) {
        super(repository);
    }
}
