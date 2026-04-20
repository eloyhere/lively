package pers.eloyhere.lively.service;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.DeleteSpecification;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pers.eloyhere.lively.entity.BaseEntity;
import pers.eloyhere.lively.repository.BaseRepository;

import java.util.*;

public class BaseService <E extends BaseEntity, R extends BaseRepository<E>>{

    protected final R repository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public BaseService(R repository) {
        this.repository = repository;
    }

    @Nonnull
    public Long count(){
        return this.repository.count();
    }

    @Nonnull
    public Long countBy(@Nullable final E entity){
        if(Objects.isNull(entity)){
            return 0L;
        }
        Example<E> example = Example.of(entity);
        return this.repository.count(example);
    }

    @Nonnull
    public Long countBy(@Nullable final Specification<E> specification){
        if(Objects.isNull(specification)){
            return 0L;
        }
        return this.repository.count(specification);
    }

    @Nonnull
    public Long countBy(@Nullable final PredicateSpecification<E> specification){
        if(Objects.isNull(specification)){
            return 0L;
        }
        return this.repository.count(specification);
    }

    @Transactional
    public void deleteById(@Nullable final UUID identity){
        if(Objects.nonNull(identity)){
            this.repository.deleteById(identity);
        }
    }

    @Transactional
    public void deleteBy(@Nullable final E entity){
        if(Objects.nonNull(entity)){
            this.repository.delete(entity);
        }
    }

    @Transactional
    public void deleteBy(@Nullable final DeleteSpecification<E> specification){
        if(Objects.nonNull(specification)){
            this.repository.delete(specification);
        }
    }

    @Transactional
    public void deleteBy(@Nullable final PredicateSpecification<E> specification){
        if(Objects.nonNull(specification)){
            this.repository.delete(specification);
        }
    }

    @Transactional
    public void deleteAll(){
        this.repository.deleteAll();
    }

    @Transactional
    public void deleteAllByIdentifiers(Iterable<UUID> identifiers){
        this.repository.deleteAllById(identifiers);
    }

    @Nonnull
    public Boolean existBy(@Nullable final E entity) {
        if (Objects.isNull(entity)) {
            return false;
        }
        Example<E> example = Example.of(entity);
        return this.repository.exists(example);
    }

    @Nonnull
    public Boolean existById(@Nullable final UUID identity) {
        if (Objects.isNull(identity)) {
            return false;
        }
        return this.repository.existsById(identity);
    }

    @Nonnull
    public Boolean existBy(@Nullable Specification<E> specification) {
        if (Objects.isNull(specification)) {
            return false;
        }
        return this.repository.count(specification) > 0;
    }

    @Nonnull
    public Boolean existBy(@Nullable PredicateSpecification<E> specification) {
        if (Objects.isNull(specification)) {
            return false;
        }
        return this.repository.count(specification) > 0;
    }

    @Nonnull
    public Optional<E> findById(@Nullable final UUID identity){
        if(Objects.isNull(identity)){
            return Optional.empty();
        }
        return this.repository.findById(identity);
    }

    @Nonnull
    public Collection<E> findAllByIdentifiers(@Nullable final Collection<UUID> identifiers){
        if(Objects.isNull(identifiers) || identifiers.isEmpty()){
            return List.of();
        }
        return this.repository.findAllById(identifiers);
    }

    @Nonnull
    public Optional<E> findOneBy(@Nullable final E entity) {
        if (Objects.isNull(entity)) {
            return Optional.empty();
        }
        Example<E> example = Example.of(entity);
        return this.repository.findOne(example);
    }

    @Nonnull
    public Optional<E> findOneBy(@Nullable final Specification<E> specification) {
        if (Objects.isNull(specification)) {
            return Optional.empty();
        }
        return this.repository.findOne(specification);
    }

    @Nonnull
    public Optional<E> findOneBy(@Nullable final PredicateSpecification<E> specification) {
        if (Objects.isNull(specification)) {
            return Optional.empty();
        }
        return this.repository.findOne(specification);
    }

    @Nonnull
    public Collection<E> findAll(){
        return this.repository.findAll();
    }

    @Nonnull
    public Collection<E> findAllBy(@Nullable final E entity){
        if(Objects.isNull(entity)){
            return List.of();
        }
        Example<E> example = Example.of(entity);
        return this.repository.findAll(example);
    }

    @Nonnull
    public Collection<E> findAllBy(@Nullable final Specification<E> specification){
        if(Objects.isNull(specification)){
            return List.of();
        }
        return this.repository.findAll(specification);
    }

    @Nonnull
    public Collection<E> findAllBy(@Nullable final PredicateSpecification<E> specification){
        if(Objects.isNull(specification)){
            return List.of();
        }
        return this.repository.findAll(specification);
    }

    @Nonnull
    public Page<E> findAllPagedBy(@Nullable final E entity, @Nullable final Integer page, @Nullable final Integer size){
        if(Objects.isNull(entity) || Objects.isNull(page) || Objects.isNull(size)){
            return Page.empty();
        }
        PageRequest pageRequest = PageRequest.of(Math.max(page, 1), Math.max(size, 1));
        Example<E> example = Example.of(entity);
        return this.repository.findAll(example, pageRequest);
    }

    @Nonnull
    public Page<E> findAllPagedBy(@Nullable final E entity, final Integer page, @Nullable final Integer size, @Nullable Sort sort){
        if(Objects.isNull(entity) || Objects.isNull(page) || Objects.isNull(size) || Objects.isNull(sort)){
            return Page.empty();
        }
        PageRequest pageRequest = PageRequest.of(Math.max(page, 1), Math.max(size, 1), sort);
        Example<E> example = Example.of(entity);
        return this.repository.findAll(example, pageRequest);
    }

    @Nonnull
    public Page<E> findAllPagedBy(@Nullable final E entity, final Integer page, @Nullable final Integer size, @Nullable Sort.Direction direction, Collection<String> properties){
        if(Objects.isNull(entity) || Objects.isNull(page) || Objects.isNull(size) || Objects.isNull(direction) || Objects.isNull(properties)){
            return Page.empty();
        }
        PageRequest pageRequest = PageRequest.of(Math.max(page, 1), Math.max(size, 1), direction, properties.toArray(String[]::new));
        Example<E> example = Example.of(entity);
        return this.repository.findAll(example, pageRequest);
    }

    @Nonnull
    public Page<E> findAllPagedBy(@Nullable final Specification<E> specification, @Nullable final Integer page, @Nullable final Integer size){
        if(Objects.isNull(specification) ||  Objects.isNull(page) || Objects.isNull(size)){
            return Page.empty();
        }
        PageRequest pageRequest = PageRequest.of(Math.max(page, 1), Math.max(size, 1));
        return this.repository.findAll(specification, pageRequest);
    }

    @Nonnull
    public Page<E> findAllPagedBy(@Nullable final Specification<E> specification, final Integer page, @Nullable final Integer size, @Nullable Sort sort){
        if(Objects.isNull(specification) || Objects.isNull(page) || Objects.isNull(size) || Objects.isNull(sort)){
            return Page.empty();
        }
        PageRequest pageRequest = PageRequest.of(Math.max(page, 1), Math.max(size, 1), sort);
        return this.repository.findAll(specification, pageRequest);
    }

    @Nonnull
    public Page<E> findAllPagedBy(@Nullable final Specification<E> specification, final Integer page, @Nullable final Integer size, @Nullable Sort.Direction direction, Collection<String> properties){
        if(Objects.isNull(specification) || Objects.isNull(page) || Objects.isNull(size) || Objects.isNull(direction) || Objects.isNull(properties)){
            return Page.empty();
        }
        PageRequest pageRequest = PageRequest.of(Math.max(page, 1), Math.max(size, 1), direction, properties.toArray(String[]::new));
        return this.repository.findAll(specification, pageRequest);
    }

    @Nonnull
    public E insert(@Nullable final E entity){
        if(Objects.nonNull(entity)){
            return this.repository.saveAndFlush(entity);
        }
        throw new NullPointerException("Entity could not be null.");
    }

    @Nonnull
    public Collection<E> insertAll(@Nullable final Collection<E> collection){
        if(Objects.nonNull(collection) && !collection.isEmpty()){
            return this.repository.saveAllAndFlush(collection);
        }
        return List.of();
    }

    @Nonnull
    public E update(@Nullable final E entity){
        if(Objects.nonNull(entity)){
            return this.repository.saveAndFlush(entity);
        }
        throw new NullPointerException("Entity could not be null.");
    }

    @Nonnull
    public Collection<E> updateAll(@Nullable final Collection<E> collection){
        if(Objects.nonNull(collection) && !collection.isEmpty()){
            return this.repository.saveAllAndFlush(collection);
        }
        return List.of();
    }
}
