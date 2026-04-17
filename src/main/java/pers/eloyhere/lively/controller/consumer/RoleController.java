package pers.eloyhere.lively.controller.consumer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.consumer.Role;
import pers.eloyhere.lively.repository.consumer.RoleRepository;
import pers.eloyhere.lively.service.consumer.RoleService;

@RestController
@RequestMapping("/role")
class RoleController extends BaseController<Role, RoleRepository, RoleService> {

    public RoleController(RoleService service) {
        super(service);
    }
}
