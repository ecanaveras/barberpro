package com.piantic.ecp.gdel.application.backend.utils.generics;

public interface SaveEventListener<T> {
    public void onSave(T entity);
}
