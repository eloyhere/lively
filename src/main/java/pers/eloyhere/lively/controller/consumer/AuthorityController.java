package pers.eloyhere.lively.controller.consumer;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.consumer.Authority;
import pers.eloyhere.lively.repository.consumer.AuthorityRepository;
import pers.eloyhere.lively.service.consumer.AuthorityService;

@RestController
@RequestMapping("authority")
class AuthorityController extends BaseController<Authority, AuthorityRepository, AuthorityService> {

    public AuthorityController(AuthorityService service) {
        super(service);
    }
}
