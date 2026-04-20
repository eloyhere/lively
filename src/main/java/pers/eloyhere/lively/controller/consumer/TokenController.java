package pers.eloyhere.lively.controller.consumer;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.consumer.Token;
import pers.eloyhere.lively.repository.consumer.TokenRepository;
import pers.eloyhere.lively.service.consumer.TokenService;

@RestController("tokenController")
@RequestMapping("/token")
class TokenController extends BaseController<Token, TokenRepository, TokenService> {

    public TokenController(TokenService service) {
        super(service);
    }
}
