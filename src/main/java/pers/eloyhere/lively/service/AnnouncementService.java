package pers.eloyhere.lively.service;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.Announcement;
import pers.eloyhere.lively.repository.AnnouncementRepository;
import pers.eloyhere.lively.repository.consumer.ConsumerRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service("announcementService")
public class AnnouncementService extends BaseService<Announcement, AnnouncementRepository> {

    public AnnouncementService(AnnouncementRepository repository) {
        super(repository);
    }
}
