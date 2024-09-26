package com.piantic.ecp.gdel.application;

import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.entity.Tenant;
import com.piantic.ecp.gdel.application.ui.views.WelcomeProfileView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.atomic.AtomicReference;

/**
 * The entry point of the Spring Boot application.
 * <p>
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */
@SpringBootApplication
@Theme(value = "barberpro")
public class Application implements AppShellConfigurator {

    public static final java.lang.String SESSION_PROFILE = "profile";
    public static final java.lang.String SESSION_TENANT = "tenand";
    public static final java.lang.String SESSION_EMAIL_ACCOUNT = "email.account";
    public static final java.lang.String SESSION_MODE_APP = "mode.app";

    public static void main(java.lang.String[] args) {

        SpringApplication.run(Application.class, args);
    }

    public static Boolean isProfileOnline() {
        AtomicReference<Boolean> isProfileOnline = new AtomicReference<>(Boolean.FALSE);
        WebStorage.getItem("barberpro.profile.online", value -> {
            isProfileOnline.set(Boolean.parseBoolean(value));
        });
        return isProfileOnline.get();
    }

    /**
     * Retorna el modo de la App (Asistido | Perfil)
     * @return
     */
    public static Integer getModeApp(){
        return (Integer) VaadinSession.getCurrent().getAttribute(SESSION_MODE_APP);
    }

    /**
     * Returna el perfil seleccionado
     * @return
     */
    public static Profile getProfile() {
        Profile profile = (Profile) VaadinSession.getCurrent().getAttribute(SESSION_PROFILE);
        if (profile == null) {
            UI.getCurrent().navigate(WelcomeProfileView.class);
        }
        return profile;
    }

    /**
     * Retorna el Tenant conectado
     * @return
     */
    public static Tenant getTenant() {
        if (VaadinSession.getCurrent() != null) {
            Tenant tenand = (Tenant) VaadinSession.getCurrent().getAttribute(SESSION_TENANT);
            if(tenand == null) {
                UI.getCurrent().navigate(WelcomeProfileView.class);
            }
            return tenand;
        }
        UI.getCurrent().navigate(WelcomeProfileView.class);
        return null;
    }

    /**
     * Devuelve el correo de la cuenta conectada
     * @return
     */
    public static String getEmailAccount() {
        //TODO Asignar el correo de la cuenta del cliente.
        String email = (String) VaadinSession.getCurrent().getAttribute(SESSION_EMAIL_ACCOUNT);
        if (email == null) {
            email = "barberpro@gmail.com";
        }
        return email;
    }

}
