package pers.eloyhere.lively.repository.consumer;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.consumer.Consumer;
import pers.eloyhere.lively.entity.consumer.Token;
import pers.eloyhere.lively.repository.BaseRepository;

import java.util.Optional;

@Repository("tokenRepository")
public interface TokenRepository extends BaseRepository<Token>, JpaSpecificationExecutor<Token> {

    Optional<Token> findBySeries(String series);

    boolean existsBySeries(String series);

    @Modifying
    @Transactional
    @Query("delete from Token t where t.consumer.username = :username")
    void deleteByUsername(String username);

    @Query("select distinct t from Token t where t.consumer.username = :username")
    Optional<Token> findByUsername(String username);
}