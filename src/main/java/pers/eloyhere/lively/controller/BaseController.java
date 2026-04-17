package pers.eloyhere.lively.controller;

import jakarta.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pers.eloyhere.lively.entity.BaseEntity;
import pers.eloyhere.lively.repository.BaseRepository;
import pers.eloyhere.lively.service.BaseService;

import java.util.Collection;
import java.util.List;
import java.util.UUID;


public class BaseController<E extends BaseEntity, R extends BaseRepository<E>, S extends BaseService<E, R>> {

    protected final S service;

    public BaseController(S service) {
        this.service = service;
    }

    @GetMapping("countBy")
    public ResponseEntity<Long> countBy(@Nonnull final E entity){
        return ResponseEntity.ok(this.service.countBy(entity));
    }

    @DeleteMapping("deleteByIdentifier")
    public ResponseEntity<String> deleteByIdentifier(@Nonnull final UUID identifier){
        this.service.deleteById(identifier);
        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("deleteBy")
    public ResponseEntity<String> deleteBy(@Nonnull final E entity){
        this.service.deleteBy(entity);
        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("deleteAllByIdentifiers")
    public ResponseEntity<String> deleteAllByIdentifiers(@Nonnull final Collection<UUID> identifiers){
        this.service.deleteAllByIdentifiers(identifiers);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("existsBy")
    public ResponseEntity<Boolean> existsBy(@Nonnull final E entity){
        return ResponseEntity.ok(this.service.existBy(entity));
    }

    @GetMapping("findByIdentifier")
    @SuppressWarnings("unchecked")
    public ResponseEntity<E> findByIdentifier(@Nonnull final UUID identifier){
        return this.service.findById(identifier).map(ResponseEntity::ok).orElse((ResponseEntity<E>) ResponseEntity.notFound());
    }

    @GetMapping("findOneBy")
    @SuppressWarnings("unchecked")
    public ResponseEntity<E> findOneBy(@Nonnull final E entity){
        return this.service.findOneBy(entity).map(ResponseEntity::ok).orElse((ResponseEntity<E>) ResponseEntity.notFound());
    }

    @GetMapping("findAllByIdentifiers")
    public ResponseEntity<Collection<E>> findAllByIdentifiers(@Nonnull final Collection<UUID> identifiers){
        return ResponseEntity.ok(this.service.findAllByIdentifiers(identifiers));
    }

    @GetMapping("findAll")
    public ResponseEntity<Collection<E>> findAll(){
        return ResponseEntity.ok(this.service.findAll());
    }

    @GetMapping("findAllBy")
    public ResponseEntity<Collection<E>> findAllBy(@Nonnull final E entity){
        return ResponseEntity.ok(this.service.findAllBy(entity));
    }
}
