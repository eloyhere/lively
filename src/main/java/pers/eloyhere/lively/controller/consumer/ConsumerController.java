package pers.eloyhere.lively.controller.consumer;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.consumer.Consumer;
import pers.eloyhere.lively.repository.consumer.ConsumerRepository;
import pers.eloyhere.lively.service.consumer.ConsumerService;

@RestController("consumerController")
@RequestMapping("/consumer")
class ConsumerController extends BaseController<Consumer, ConsumerRepository, ConsumerService> {

    public ConsumerController(ConsumerService service) {
        super(service);
    }
}
