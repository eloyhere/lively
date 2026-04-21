package pers.eloyhere.lively.service.consumer;

import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.consumer.Menu;
import pers.eloyhere.lively.repository.consumer.MenuRepository;
import pers.eloyhere.lively.service.BaseService;

@Service("menuService")
public class MenuService extends BaseService<Menu, MenuRepository> {

    public MenuService(MenuRepository repository) {
        super(repository);
    }
}
