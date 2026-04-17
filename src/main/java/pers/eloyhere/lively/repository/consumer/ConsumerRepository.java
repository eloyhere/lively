package pers.eloyhere.lively.repository.consumer;

import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.consumer.Consumer;
import pers.eloyhere.lively.repository.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository("consumerRepository")
public interface ConsumerRepository extends BaseRepository<Consumer> {


    List<Consumer> deleteByUsername(String username);

    Boolean existsByUsername(String username);

    Optional<Consumer> findByUsername(String username);

}