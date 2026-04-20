package pers.eloyhere.lively.service.consumer;

import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.consumer.Client;
import pers.eloyhere.lively.repository.consumer.ClientRepository;
import pers.eloyhere.lively.service.BaseService;

@Service("clientService")
public class ClientService extends BaseService<Client, ClientRepository> {

    public ClientService(ClientRepository clientRepository) {
        super(clientRepository);
    }
}
