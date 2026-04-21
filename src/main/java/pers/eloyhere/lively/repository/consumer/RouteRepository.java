package pers.eloyhere.lively.repository.consumer;

import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.consumer.Route;
import pers.eloyhere.lively.repository.BaseRepository;

@Repository("routeRepository")
public interface RouteRepository extends BaseRepository<Route> {
}