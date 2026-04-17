package pers.eloyhere.lively.service;

import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.Invitation;
import pers.eloyhere.lively.repository.InvitationRepository;

@Service("invitationService")
public class InvitationService extends BaseService<Invitation, InvitationRepository> {

    public InvitationService(InvitationRepository repository) {
        super(repository);
    }
}
