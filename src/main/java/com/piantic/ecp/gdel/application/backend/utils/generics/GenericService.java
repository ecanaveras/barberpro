package com.piantic.ecp.gdel.application.backend.utils.generics;

import java.util.List;

public interface GenericService<T> {

    List<T> findAll(String filter);
    Long getId(T entity);
}
