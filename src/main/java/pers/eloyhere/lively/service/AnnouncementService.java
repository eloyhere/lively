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

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service("announcementService")
public class AnnouncementService extends BaseService<Announcement, AnnouncementRepository> {

    protected final ConsumerRepository consumerRepository;

    public AnnouncementService(AnnouncementRepository repository, ConsumerRepository consumerRepository) {
        super(repository);
        this.consumerRepository = consumerRepository;
    }

    @Override
    public @NonNull Collection<Announcement> findAll() {
        Collection<Announcement> collection = super.findAll();
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if(Objects.isNull(authentication)){
            return collection;
        }
        consumerRepository.findByUsername(authentication.getName()).ifPresent((consumer) -> {
            collection.forEach((announcement) -> {
                announcement.add(consumer);
            });
        });
        return this.repository.saveAllAndFlush(collection);
    }

    @Override
    public @NonNull Collection<Announcement> findAllBy(@Nullable Announcement entity) {
        Collection<Announcement> collection = super.findAllBy(entity);
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if(Objects.isNull(authentication)){
            return collection;
        }
        consumerRepository.findByUsername(authentication.getName()).ifPresent((consumer) -> {
            collection.forEach((announcement) -> {
                announcement.add(consumer);
            });
        });
        return this.repository.saveAllAndFlush(collection);
    }

    @Override
    public @NonNull Collection<Announcement> findAllBy(@Nullable Specification<Announcement> specification) {
        Collection<Announcement> collection = super.findAllBy(specification);
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if(Objects.isNull(authentication)){
            return collection;
        }
        consumerRepository.findByUsername(authentication.getName()).ifPresent((consumer) -> {
            collection.forEach((announcement) -> {
                announcement.add(consumer);
            });
        });
        return this.repository.saveAllAndFlush(collection);
    }

    @Override
    public @NonNull Collection<Announcement> findAllBy(@Nullable PredicateSpecification<Announcement> specification) {
        Collection<Announcement> collection = super.findAllBy(specification);
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if(Objects.isNull(authentication)){
            return collection;
        }
        consumerRepository.findByUsername(authentication.getName()).ifPresent((consumer) -> {
            collection.forEach((announcement) -> {
                announcement.add(consumer);
            });
        });
        return this.repository.saveAllAndFlush(collection);
    }

    @Override
    public @NonNull Collection<Announcement> findAllByIdentifiers(@Nullable Collection<UUID> identifiers) {
        Collection<Announcement> collection = super.findAllByIdentifiers(identifiers);
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if(Objects.isNull(authentication)){
            return collection;
        }
        consumerRepository.findByUsername(authentication.getName()).ifPresent((consumer) -> {
            collection.forEach((announcement) -> {
                announcement.add(consumer);
            });
        });
        return this.repository.saveAllAndFlush(collection);
    }

    @Override
    public @NonNull Page<Announcement> findAllPagedBy(@Nullable Announcement entity, @Nullable Integer page, @Nullable Integer size) {
        Page<Announcement> p = super.findAllPagedBy(entity, page, size);
        return p.map(this.repository::saveAndFlush);
    }

    @Override
    public @NonNull Page<Announcement> findAllPagedBy(@Nullable Specification<Announcement> specification, @Nullable Integer page, @Nullable Integer size) {
        Page<Announcement> p = super.findAllPagedBy(specification, page, size);
        return p.map(this.repository::saveAndFlush);
    }

    @Override
    public @NonNull Page<Announcement> findAllPagedBy(@Nullable Announcement entity, Integer page, @Nullable Integer size, @Nullable Sort sort) {
        Page<Announcement> p = super.findAllPagedBy(entity, page, size, sort);
        return p.map(this.repository::saveAndFlush);
    }

    @Override
    public @NonNull Page<Announcement> findAllPagedBy(@Nullable Specification<Announcement> specification, Integer page, @Nullable Integer size, @Nullable Sort sort) {
        Page<Announcement> p = super.findAllPagedBy(specification, page, size, sort);
        return p.map(this.repository::saveAndFlush);
    }

    @Override
    public @NonNull Page<Announcement> findAllPagedBy(@Nullable Announcement entity, Integer page, @Nullable Integer size, Sort.@Nullable Direction direction, Collection<String> properties) {
        Page<Announcement> p = super.findAllPagedBy(entity, page, size, direction, properties);
        return p.map(this.repository::saveAndFlush);
    }

    @Override
    public @NonNull Page<Announcement> findAllPagedBy(@Nullable Specification<Announcement> specification, Integer page, @Nullable Integer size, Sort.@Nullable Direction direction, Collection<String> properties) {
        Page<Announcement> p = super.findAllPagedBy(specification, page, size, direction, properties);
        return p.map(this.repository::saveAndFlush);
    }

    @Override
    public @NonNull Optional<Announcement> findById(@Nullable UUID identity) {
        Optional<Announcement> announcement = super.findById(identity);
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return announcement.map((entity) -> {
            if(Objects.nonNull(authentication)){
                consumerRepository.findByUsername(authentication.getName()).ifPresent(entity::add);
                return this.repository.saveAndFlush(entity);
            }else{
                return entity;
            }
        });
    }

    @Override
    public @NonNull Optional<Announcement> findOneBy(@Nullable Announcement entity) {
        Optional<Announcement> announcement = super.findOneBy(entity);
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return announcement.map((e) -> {
            if(Objects.nonNull(authentication)){
                consumerRepository.findByUsername(authentication.getName()).ifPresent(e::add);
                return this.repository.saveAndFlush(e);
            }else{
                return e;
            }
        });
    }

    @Override
    public @NonNull Optional<Announcement> findOneBy(@Nullable Specification<Announcement> specification) {
        Optional<Announcement> announcement = super.findOneBy(specification);
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return announcement.map((e) -> {
            if(Objects.nonNull(authentication)){
                consumerRepository.findByUsername(authentication.getName()).ifPresent(e::add);
                return this.repository.saveAndFlush(e);
            }else{
                return e;
            }
        });
    }

    @Override
    public @NonNull Optional<Announcement> findOneBy(@Nullable PredicateSpecification<Announcement> specification) {
        Optional<Announcement> announcement = super.findOneBy(specification);
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return announcement.map((e) -> {
            if(Objects.nonNull(authentication)){
                consumerRepository.findByUsername(authentication.getName()).ifPresent(e::add);
                return this.repository.saveAndFlush(e);
            }else{
                return e;
            }
        });
    }
}
