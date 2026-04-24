package pers.eloyhere.lively.repository.game;

import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.game.Level;
import pers.eloyhere.lively.repository.BaseRepository;

@Repository("levelRepository")
public interface LevelRepository extends BaseRepository<Level> {
}