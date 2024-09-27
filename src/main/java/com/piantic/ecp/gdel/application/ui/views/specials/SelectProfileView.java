package com.piantic.ecp.gdel.application.ui.views.specials;

import com.piantic.ecp.gdel.application.Application;
import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.service.ProfileService;
import com.piantic.ecp.gdel.application.backend.utils.MessagesUtil;
import com.piantic.ecp.gdel.application.ui.views.MainLayout;
import com.piantic.ecp.gdel.application.ui.views.WizardConfigView;
import com.piantic.ecp.gdel.application.ui.views.WorkingView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.List;

@Route(value = "select-profile", layout = MainLayout.class)
public class SelectProfileView extends Div {


    public SelectProfileView(ProfileService profileService) {

        Div divtitle = new Div();
        H3 welcome = new H3("MODO ASISTIDO");
        H5 h5 = new H5("Selecciona el Perfil que deseas usar");

        Div divcontent = new Div(welcome);
        divcontent.addClassName("welcome-content");

        Div divcards = new Div();
        divcards.addClassName("welcome-cards");

        List<Profile> resulquery = profileService.findProfilesActives();
        resulquery.forEach(profile -> {
            divcards.add(getCardProfile(profile));
        });

        if (!resulquery.isEmpty()) {
            divtitle.add(welcome, h5);
            divcontent.add(divtitle);
            divcontent.add(divcards);
            add(divcontent);
        } else {
            divtitle.add(welcome);
            divcontent.add(divtitle);
            Div div = MessagesUtil.showWarning("No hay perfiles activos en tu cuenta, por favor configura tu Negocio!");
            div.addClassNames(LumoUtility.TextTransform.UPPERCASE);

            Button btngoto = new Button("Asistente de Configuración", LineAwesomeIcon.HAND_POINT_UP.create());
            btngoto.addThemeVariants(ButtonVariant.LUMO_SMALL);
            btngoto.addClickListener(event -> getUI().ifPresent(ui -> ui.navigate(WizardConfigView.class)));
//            divcontent.add(getUINewProfile());
            divcontent.add(new VerticalLayout(div, btngoto));
            add(divcontent);
        }
    }

    private Component getCardProfile(Profile profile) {
        VerticalLayout vl = new VerticalLayout();
        vl.addClassName("card-profile");
        vl.addClassNames(LumoUtility.BoxShadow.MEDIUM);
        Avatar avatar = new Avatar(profile.getNameProfile());
        H4 h4 = new H4(profile.getNameProfile());
        vl.add(avatar, h4);
        vl.addClickListener(e -> {
            VaadinSession.getCurrent().setAttribute(Application.SESSION_PROFILE, profile);
            UI.getCurrent().navigate(WorkingView.class);
        });
        return vl;
    }


}
