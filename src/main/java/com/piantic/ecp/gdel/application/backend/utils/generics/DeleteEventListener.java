package com.piantic.ecp.gdel.application.backend.utils.generics;

public interface DeleteEventListener<T> {
    void onDelete(T entity);
}
