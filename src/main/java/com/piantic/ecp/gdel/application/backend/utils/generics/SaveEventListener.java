package com.piantic.ecp.gdel.application.backend.utils.generics;

public interface SaveEventListener<T> {
    void onSave(T entity);
}
