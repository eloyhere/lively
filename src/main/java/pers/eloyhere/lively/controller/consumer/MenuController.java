package pers.eloyhere.lively.controller.consumer;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.consumer.Menu;
import pers.eloyhere.lively.repository.consumer.MenuRepository;
import pers.eloyhere.lively.service.consumer.MenuService;

@RestController("menuController")
@RequestMapping("/menu")
class MenuController extends BaseController<Menu, MenuRepository, MenuService> {

    public MenuController(MenuService service) {
        super(service);
    }
}
