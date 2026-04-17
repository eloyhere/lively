package pers.eloyhere.lively.repository;

import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.Invitation;

@Repository("invitationRepository")
public interface InvitationRepository extends BaseRepository<Invitation> {
}