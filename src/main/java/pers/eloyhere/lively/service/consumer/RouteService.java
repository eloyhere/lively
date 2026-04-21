package pers.eloyhere.lively.service.consumer;

import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.consumer.Route;
import pers.eloyhere.lively.repository.consumer.RouteRepository;
import pers.eloyhere.lively.service.BaseService;

@Service("routeService")
public class RouteService extends BaseService<Route, RouteRepository> {

    public RouteService(RouteRepository repository) {
        super(repository);
    }
}
