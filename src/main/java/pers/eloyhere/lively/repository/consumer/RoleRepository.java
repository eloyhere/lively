package pers.eloyhere.lively.repository.consumer;

import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.consumer.Role;
import pers.eloyhere.lively.repository.BaseRepository;

@Repository("roleRepository")
public interface RoleRepository extends BaseRepository<Role> {
}