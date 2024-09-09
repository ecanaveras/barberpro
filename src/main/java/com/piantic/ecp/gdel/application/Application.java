package com.piantic.ecp.gdel.application;

import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.ui.views.WelcomeView;
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
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "barberpro")
public class Application implements AppShellConfigurator {

    public static final String SESSION_PROFILE = "profile";

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

    public static Boolean isProfileOnline() {
        AtomicReference<Boolean> isProfileOnline = new AtomicReference<>(Boolean.FALSE);
        WebStorage.getItem("barberpro.profile.online", value -> {
            isProfileOnline.set(Boolean.parseBoolean(value));
        });
        return isProfileOnline.get();
    }


    public static Profile getProfile(){
        Profile profile = (Profile) VaadinSession.getCurrent().getAttribute(SESSION_PROFILE);
        if(profile == null){
            UI.getCurrent().navigate(WelcomeView.class);
        }
        return profile;
    }

}
