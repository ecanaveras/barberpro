package com.piantic.ecp.gdel.application.ui.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("welcome")
public class WelcomeMain extends Div {

    public WelcomeMain() {
        addClassName("welcome-main");

        Div divtitle = new Div();
        H3 welcome = new H3("Bienvenido a BarberPro");
        H5 h5 = new H5("La APP que te ayuda gestionar tu Negocio!");

        Span info = new Span("La APP que te ayuda gestionar tu Negocio!");
        info.addClassNames(LumoUtility.Margin.MEDIUM, LumoUtility.Margin.SMALL, LumoUtility.TextColor.PRIMARY);


        divtitle.add(welcome, h5);

        add(divtitle);
    }
}
