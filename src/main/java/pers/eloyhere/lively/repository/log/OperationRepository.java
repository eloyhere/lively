package pers.eloyhere.lively.repository.log;

import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.log.Operation;
import pers.eloyhere.lively.repository.BaseRepository;

@Repository("operationRepository")
public interface OperationRepository extends BaseRepository<Operation> {
}