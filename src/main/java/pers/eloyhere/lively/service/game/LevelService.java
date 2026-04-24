package pers.eloyhere.lively.service.game;

import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.game.Level;
import pers.eloyhere.lively.repository.game.LevelRepository;
import pers.eloyhere.lively.service.BaseService;

@Service("levelService")
public class LevelService extends BaseService<Level, LevelRepository> {

    public LevelService(LevelRepository repository) {
        super(repository);
    }
}
