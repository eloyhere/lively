package pers.eloyhere.lively.repository.consumer;

import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.consumer.Authority;
import pers.eloyhere.lively.repository.BaseRepository;

@Repository("authorityRepository")
public interface AuthorityRepository extends BaseRepository<Authority> {


}