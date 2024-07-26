package com.piantic.ecp.gdel.application.utils.generics;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.shared.Registration;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FormEvents {

    public static abstract class Event<T extends Component> extends ComponentEvent<T> {

        private final Object data;

        protected Event(T source, Object data) {
            super(source, false);
            this.data = data;
        }

        public Object getData() {
            return data;
        }
    }

    public static class SaveEvent<T extends Component> extends Event<T> {
        public SaveEvent(T source, Object data) {
            super(source, data);
        }
    }

    public static class DeleteEvent<T extends Component> extends Event<T> {
        public DeleteEvent(T source, Object data) {
            super(source, data);
        }
    }

    public static class CloseEvent<T extends Component> extends Event<T> {
        public CloseEvent(T source) {
            super(source, null);
        }
    }

    public static <T extends ComponentEvent<?>> Registration addListener(Component source, Class<T> event, ComponentEventListener<T> listener) {
        try {
            Field eventBusField = Component.class.getDeclaredField("eventBus");
            eventBusField.setAccessible(true);
            Object eventBus = eventBusField.get(source);
            Method addListenerMethod = eventBus.getClass().getMethod("addListener", Class.class, ComponentEventListener.class);
            return (Registration) addListenerMethod.invoke(eventBus, event, listener);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access EventBus", e);
        }
    }
}
