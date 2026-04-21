package pers.eloyhere.lively.repository.consumer;

import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.consumer.Menu;
import pers.eloyhere.lively.repository.BaseRepository;

@Repository("menuRepository")
public interface MenuRepository extends BaseRepository<Menu> {
}