package com.piantic.ecp.gdel.application.backend.repository;

public interface DeleteEventListener<T> {
    public void onDelete(T entity);
}
