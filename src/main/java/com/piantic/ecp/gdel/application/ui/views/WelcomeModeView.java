package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.Application;
import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.utils.MessagesUtil;
import com.piantic.ecp.gdel.application.ui.views.specials.HomeView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;

@PageTitle("Bienvenido | BarberPro")
@Route("welcome-mode")
public class WelcomeModeView extends Div {

    public WelcomeModeView() {
        addClassName("welcome-view");

        Div divtitle = new Div();
        H3 welcome = new H3("Bienvenido a BarberPro");
        H5 h5 = new H5("Selecciona el modo de Trabajo para continuar");

        Div divcontent = new Div(welcome);
        divcontent.addClassName("welcome-content");

        Div divcards = new Div();
        divcards.addClassName("welcome-cards");


        divcards.add(getCardMode("ASISTIDO", "Una persona centraliza el registro de todas las operaciones de los perfiles. \nIdeal para cuando solo hay un dispositivo disponible."));
        divcards.add(getCardMode("PERFIL", "Cada perfil registra sus propias operaciones directamente en un dispositivo."));


        divtitle.add(welcome, h5);
        divcontent.add(divtitle);
        divcontent.add(divcards);
        add(divcontent);


    }


    /**
     * Tarjeta del Modo Asistido
     *
     * @return
     */
    private Component getCardMode(String nameMode, String message) {
        VerticalLayout vl = new VerticalLayout();
        vl.addClassName("card-welcome-mode");
        vl.addClassNames(LumoUtility.BoxShadow.MEDIUM);
        Avatar avatar = new Avatar(nameMode);
        H4 h4 = new H4(nameMode);
        Div divmessage = null;
        if (nameMode.contains("ASISTIDO"))
            divmessage = MessagesUtil.showPrimary(message);
        else
            divmessage = MessagesUtil.showSucces(message);

        vl.add(avatar, h4, divmessage);

        vl.addClickListener(e -> {
            if (nameMode.contains("ASISTIDO")) {
                VaadinSession.getCurrent().setAttribute(Application.SESSION_MODE_APP, 1);
                UI.getCurrent().navigate(HomeView.class);
            } else {
                VaadinSession.getCurrent().setAttribute(Application.SESSION_MODE_APP, 2);
                UI.getCurrent().navigate(WelcomeProfileView.class);
            }
        });
        return vl;
    }

    private void saveLocalSession() {
        WebStorage.setItem("barberpro.profile.online", String.valueOf(true));
    }

    private void continueToMainLayout(Profile profile) {
        VaadinSession.getCurrent().setAttribute(Application.SESSION_PROFILE, profile);
        getUI().ifPresent(ui -> ui.navigate(HomeView.class));
        saveLocalSession();
    }
}
