package com.piantic.ecp.gdel.application.ui.views;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.time.LocalDate;

@Route("welcome")
public class WelcomeMain extends Div {

    public WelcomeMain() {
        addClassName("welcome-main");


        Div divcontent = new Div();
        divcontent.addClassName("welcome-content");

        Div divtitle = new Div();
        divtitle.addClassName("welcome-title");

        H1 barberPro = new H1("BarberPro");
        barberPro.addClassName("app-name");

        // Add image
        //Image img = new Image("images/logo.png", "Logo");
        //img.setHeight("100px");

        H3 welcome = new H3("Bienvenido a BarberPro");
        H5 h5 = new H5("La aplicación que te ayuda gestionar tu Negocio!");
        Span info = new Span("Con BarberPro podras gestionar tu negocio de forma segura y rápida, sin complicaciones");
        Span info2 = new Span("Para empezar vamos a ayudarte a configurar tu Negocio, es muy sencillo...");

        Button continueButton = new Button("EMPEZEMOS", LineAwesomeIcon.ROCKET_SOLID.create());
        continueButton.setIconAfterText(true);
        continueButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_ICON);
        continueButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(WizardConfigView.class)));

        divtitle.add(welcome, h5, info, info2);

        Div footer = new Div(new Html("<p>© " + LocalDate.now().getYear() + ". <b>BarberPro</b>. Todos los derechos reservados. <br>Una idea de <b>Piantic S.A.S</b>. Powered by <b>Vaadin</b>.</p>"));
        footer.addClassName("footer");

        divcontent.add(barberPro, divtitle, continueButton);

        add(divcontent, footer);
    }
}
