package pers.eloyhere.lively.controller.consumer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.consumer.Route;
import pers.eloyhere.lively.repository.consumer.RouteRepository;
import pers.eloyhere.lively.service.consumer.RouteService;

@RestController("routeController")
@RequestMapping("/route")
public class RouteController extends BaseController<Route, RouteRepository, RouteService> {

    public RouteController(RouteService service) {
        super(service);
    }
}
