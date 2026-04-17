package pers.eloyhere.lively.repository.consumer;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.consumer.Token;
import pers.eloyhere.lively.repository.BaseRepository;

import java.util.Optional;

@Repository("tokenRepository")
public interface TokenRepository extends BaseRepository<Token>, JpaSpecificationExecutor<Token> {

    Optional<Token> findBySeries(String series);

    boolean existsBySeries(String series);
}