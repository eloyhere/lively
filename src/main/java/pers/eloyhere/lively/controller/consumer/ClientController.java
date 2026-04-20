package pers.eloyhere.lively.controller.consumer;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.consumer.Client;
import pers.eloyhere.lively.repository.consumer.ClientRepository;
import pers.eloyhere.lively.service.consumer.ClientService;

@RestController("clientController")
@RequestMapping("/client")
class ClientController extends BaseController<Client, ClientRepository, ClientService> {

    public ClientController(ClientService service) {
        super(service);
    }
}
