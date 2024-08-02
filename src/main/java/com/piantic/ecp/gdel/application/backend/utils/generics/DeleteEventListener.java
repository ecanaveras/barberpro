package com.piantic.ecp.gdel.application.backend.utils.generics;

public interface DeleteEventListener<T> {
    public void onDelete(T entity);
}
