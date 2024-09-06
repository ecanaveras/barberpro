package com.piantic.ecp.gdel.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.WebStorage;
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

}
