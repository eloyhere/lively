package pers.eloyhere.lively.controller.game;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.game.Level;
import pers.eloyhere.lively.repository.game.LevelRepository;
import pers.eloyhere.lively.service.game.LevelService;

@RestController("levelController")
@RequestMapping("level")
class LevelController extends BaseController<Level, LevelRepository, LevelService> {

    public LevelController(LevelService service) {
        super(service);
    }
}
