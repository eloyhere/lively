package pers.eloyhere.lively.controller.consumer;

import jakarta.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.consumer.Consumer;
import pers.eloyhere.lively.repository.consumer.ConsumerRepository;
import pers.eloyhere.lively.service.consumer.ConsumerService;

import java.util.UUID;

@RestController("consumerController")
@RequestMapping("/consumer")
class ConsumerController extends BaseController<Consumer, ConsumerRepository, ConsumerService> {

    public ConsumerController(ConsumerService service) {
        super(service);
    }

    @PutMapping("addRole")
    @SuppressWarnings("unchecked")
    public ResponseEntity<Consumer> addRole(@Nonnull final UUID consumer, @Nonnull final UUID role){
        return this.service.addRole(consumer, role).map(ResponseEntity::ok).orElse((ResponseEntity<Consumer>) ResponseEntity.notFound());
    }

    @PutMapping("removeRole")
    @SuppressWarnings("unchecked")
    public ResponseEntity<Consumer> removeRole(@Nonnull final UUID consumer, @Nonnull final UUID role){
        return this.service.removeRole(consumer, role).map(ResponseEntity::ok).orElse((ResponseEntity<Consumer>) ResponseEntity.notFound());
    }
}
