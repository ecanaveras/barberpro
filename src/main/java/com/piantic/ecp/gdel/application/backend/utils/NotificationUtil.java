package com.piantic.ecp.gdel.application.backend.utils;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class NotificationUtil {

    // Método para mostrar notificación de éxito
    public static void showSuccess(String message) {
        Notification notification = createNotification(message, NotificationVariant.LUMO_SUCCESS, VaadinIcon.CHECK_CIRCLE);
        notification.open();
    }

    // Método para mostrar notificación de error
    public static void showError(String message) {
        Notification notification = createNotification(message, NotificationVariant.LUMO_ERROR, VaadinIcon.EXCLAMATION_CIRCLE);
        notification.open();
    }

    // Método para mostrar notificación de advertencia
    public static void showWarning(String message) {
        Notification notification = createNotification(message, NotificationVariant.LUMO_CONTRAST, VaadinIcon.WARNING);
        notification.open();
    }

    // Método para mostrar notificación de información
    public static void showInfo(String message) {
        Notification notification = createNotification(message, NotificationVariant.LUMO_PRIMARY, VaadinIcon.INFO_CIRCLE);
        notification.open();
    }

    // Método para mostrar notificación de contraste
    public static void showContrast(String message) {
        Notification notification = createNotification(message, NotificationVariant.LUMO_CONTRAST, VaadinIcon.INFO_CIRCLE);
        notification.open();
    }

    // Método para mostrar notificación de contraste
    public static void showContrastCloseable(String message) {
        Notification notification = createNotificationContrast(message);
        notification.open();
    }

    private static Notification createNotificationContrast(String message) {
        Notification noti = new Notification();
        noti.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
        Div text = new Div(new Text(message));
        Button closeBtn = new Button(new Icon("lumo", "cross"));
        closeBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeBtn.setAriaLabel("Cerrar");
        closeBtn.addClickListener(e -> {
            noti.close();
        });
        HorizontalLayout layout = new HorizontalLayout(text, closeBtn);
        layout.setAlignSelf(FlexComponent.Alignment.CENTER);

        noti.add(layout);
        noti.setPosition(Notification.Position.MIDDLE);

        return noti;
    }

    // Método privado para crear la notificación
    private static Notification createNotification(String message, NotificationVariant variant, VaadinIcon icon) {
        Notification notification = new Notification();
        notification.add(new HorizontalLayout(icon.create(), new Text(message)));
        notification.addThemeVariants(variant);
        notification.setDuration(3000); // Puedes cambiar la duración si lo necesitas
        return notification;
    }
}

