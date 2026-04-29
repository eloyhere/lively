package pers.eloyhere.lively.controller;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pers.eloyhere.lively.annotation.Administrator;
import pers.eloyhere.lively.annotation.Authenticated;
import pers.eloyhere.lively.annotation.Everyone;
import pers.eloyhere.lively.annotation.Unauthenticated;
import pers.eloyhere.lively.entity.BaseEntity;
import pers.eloyhere.lively.entity.consumer.Consumer;
import pers.eloyhere.lively.repository.BaseRepository;
import pers.eloyhere.lively.service.BaseService;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class BaseController<E extends BaseEntity, R extends BaseRepository<E>, S extends BaseService<E, R>> {

    protected final S service;

    public BaseController(S service) {
        this.service = service;
    }

    @Administrator
    @GetMapping("countBy")
    public ResponseEntity<Long> countBy(@Nonnull final E entity){
        return ResponseEntity.ok(this.service.countBy(entity));
    }

    @Administrator
    @DeleteMapping("deleteAll")
    public ResponseEntity<String> deleteAll(){
        this.service.deleteAll();
        return ResponseEntity.ok("ok");
    }

    @Administrator
    @DeleteMapping("deleteByIdentifier")
    public ResponseEntity<String> deleteByIdentifier(@Nonnull final UUID identifier){
        this.service.deleteById(identifier);
        return ResponseEntity.ok("ok");
    }

    @Administrator
    @DeleteMapping("deleteBy")
    public ResponseEntity<String> deleteBy(@Nonnull final E entity){
        this.service.deleteBy(entity);
        return ResponseEntity.ok("ok");
    }

    @Administrator
    @DeleteMapping("deleteAllByIdentifiers")
    public ResponseEntity<String> deleteAllByIdentifiers(@Nonnull final Collection<UUID> identifiers){
        this.service.deleteAllByIdentifiers(identifiers);
        return ResponseEntity.ok("ok");
    }

    @Administrator
    @GetMapping("existsBy")
    public ResponseEntity<Boolean> existsBy(@Nonnull final E entity){
        return ResponseEntity.ok(this.service.existBy(entity));
    }

    @Administrator
    @GetMapping("findByIdentifier")
    @SuppressWarnings("unchecked")
    public ResponseEntity<E> findByIdentifier(@Nonnull final UUID identifier){
        return this.service.findById(identifier).map(ResponseEntity::ok).orElse((ResponseEntity<E>) ResponseEntity.notFound());
    }

    @Administrator
    @GetMapping("findOneBy")
    @SuppressWarnings("unchecked")
    public ResponseEntity<E> findOneBy(@Nonnull final E entity){
        return this.service.findOneBy(entity).map(ResponseEntity::ok).orElse((ResponseEntity<E>) ResponseEntity.notFound());
    }

    @Administrator
    @GetMapping("findAllByIdentifiers")
    public ResponseEntity<Collection<E>> findAllByIdentifiers(@Nonnull final Collection<UUID> identifiers){
        return ResponseEntity.ok(this.service.findAllByIdentifiers(identifiers));
    }

    @Administrator
    @GetMapping("findAll")
    public ResponseEntity<Collection<E>> findAll(){
        return ResponseEntity.ok(this.service.findAll());
    }

    @Administrator
    @GetMapping("findAllBy")
    public ResponseEntity<Collection<E>> findAllBy(@Nonnull final E entity){
        return ResponseEntity.ok(this.service.findAllBy(entity));
    }

    @Authenticated
    @GetMapping("findAllPagedBy")
    public ResponseEntity<Page<E>> findAllPagedBy(@Nonnull final E entity, @Nonnull @RequestParam final Integer page, @Nonnull @RequestParam Integer size, Sort.Direction direction, Collection<String> properties){
        if(Objects.isNull(direction) || Objects.isNull(properties)){
            return ResponseEntity.ok(this.service.findAllPagedBy(entity, page, size));
        }
        return ResponseEntity.ok(this.service.findAllPagedBy(entity, page, size, direction, properties));
    }

    @Administrator
    @PutMapping("insert")
    public ResponseEntity<E> insert(@Nonnull final E entity){
        return ResponseEntity.ok(this.service.insert(entity));
    }

    @Administrator
    @PutMapping("update")
    public ResponseEntity<E> update(@Nonnull final E entity){
        return ResponseEntity.ok(this.service.update(entity));
    }
}
