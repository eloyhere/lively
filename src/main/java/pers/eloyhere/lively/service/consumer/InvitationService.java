package pers.eloyhere.lively.service.consumer;

import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.consumer.Invitation;
import pers.eloyhere.lively.repository.consumer.InvitationRepository;
import pers.eloyhere.lively.service.BaseService;

@Service("invitationService")
public class InvitationService extends BaseService<Invitation, InvitationRepository> {

    public InvitationService(InvitationRepository repository) {
        super(repository);
    }
}
