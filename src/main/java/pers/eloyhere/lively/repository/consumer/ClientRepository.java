package pers.eloyhere.lively.repository.consumer;

import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.consumer.Client;
import pers.eloyhere.lively.repository.BaseRepository;

@Repository("clientRepository")
public interface ClientRepository extends BaseRepository<Client> {
}