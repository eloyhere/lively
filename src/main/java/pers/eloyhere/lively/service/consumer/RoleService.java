package pers.eloyhere.lively.service.consumer;

import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.consumer.Role;
import pers.eloyhere.lively.repository.consumer.RoleRepository;
import pers.eloyhere.lively.service.BaseService;

@Service("roleService")
public class RoleService extends BaseService<Role, RoleRepository> {
    public RoleService(RoleRepository repository) {
        super(repository);
    }
}
