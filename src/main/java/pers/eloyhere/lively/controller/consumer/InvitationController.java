package pers.eloyhere.lively.controller.consumer;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.consumer.Invitation;
import pers.eloyhere.lively.repository.consumer.InvitationRepository;
import pers.eloyhere.lively.service.consumer.InvitationService;

@RestController("invitationController")
@RequestMapping("/invitation")
class InvitationController extends BaseController<Invitation, InvitationRepository, InvitationService> {

    public InvitationController(InvitationService service) {
        super(service);
    }
}
