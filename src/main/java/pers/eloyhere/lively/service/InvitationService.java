package pers.eloyhere.lively.service;

import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.consumer.Invitation;
import pers.eloyhere.lively.repository.consumer.InvitationRepository;

@Service("invitationService")
public class InvitationService extends BaseService<Invitation, InvitationRepository> {

    public InvitationService(InvitationRepository repository) {
        super(repository);
    }
}
