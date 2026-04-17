package pers.eloyhere.lively.repository.consumer;

import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.consumer.Invitation;
import pers.eloyhere.lively.repository.BaseRepository;

@Repository("invitationRepository")
public interface InvitationRepository extends BaseRepository<Invitation> {
}