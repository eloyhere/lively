package pers.eloyhere.lively.repository.chat;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.chat.Chat;
import pers.eloyhere.lively.repository.BaseRepository;

import java.util.List;
import java.util.UUID;

@Repository("chatRepository")
public interface ChatRepository extends BaseRepository<Chat> {
    @Query("select c from Chat c where c.consumer.id = :id")
    List<Chat> findByConsumer_Id(@Param("id") UUID id);

    @Query("select c from Chat c where c.consumer.username = :username")
    List<Chat> findByConsumer_Username(@Param("username") String username);
}