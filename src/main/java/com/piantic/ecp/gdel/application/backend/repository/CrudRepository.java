package com.piantic.ecp.gdel.application.backend.repository;

import org.springframework.stereotype.Repository;

import java.io.Serializable;

public interface CrudRepository<T, ID extends Serializable> extends Repository {

    <S extends T> S save(S entity);

    T findOne(ID primaryKey);

    Iterable<T> findAll();

    Long count();

    void delete(T entity);

    boolean exists(ID primaryKey);
}
