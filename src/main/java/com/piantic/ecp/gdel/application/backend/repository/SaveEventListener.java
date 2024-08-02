package com.piantic.ecp.gdel.application.backend.repository;

public interface SaveEventListener<T> {
    public void onSave(T entity);
}
