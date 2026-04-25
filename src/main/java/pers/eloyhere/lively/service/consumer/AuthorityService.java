package pers.eloyhere.lively.service.consumer;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.consumer.Authority;
import pers.eloyhere.lively.repository.consumer.AuthorityRepository;
import pers.eloyhere.lively.service.BaseService;

@Service("authorityService")
public class AuthorityService extends BaseService<Authority, AuthorityRepository> {

    public AuthorityService(AuthorityRepository repository) {
        super(repository);
    }
}
