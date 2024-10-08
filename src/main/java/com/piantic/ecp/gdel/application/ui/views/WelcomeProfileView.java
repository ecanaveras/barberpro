package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.Application;
import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.service.ProfileService;
import com.piantic.ecp.gdel.application.backend.utils.MessagesUtil;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

@PageTitle("Bienvenido | BarberPro")
@Route("welcome-profile")
public class WelcomeProfileView extends Div {


    public WelcomeProfileView(ProfileService profileService) {
        addClassName("welcome-view");

        Div divtitle = new Div();
        H3 welcome = new H3("Bienvenido a BarberPro");
        H5 h5 = new H5("Selecciona tu Perfil para continuar");

        Div divcontent = new Div(welcome);
        divcontent.addClassName("welcome-content");

        Div divcards = new Div();
        divcards.addClassName("welcome-cards");

        profileService.findProfilesActives().forEach(profile -> {
            divcards.add(getCardProfile(profile));
        });

        if (!profileService.findAll().isEmpty()) {
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


    /**
     * Tarjeta del Perfil
     *
     * @param profile
     * @return
     */
    private Component getCardProfile(Profile profile) {
        VerticalLayout vl = new VerticalLayout();
        vl.addClassName("card-profile");
        vl.addClassNames(LumoUtility.BoxShadow.MEDIUM);
        Avatar avatar = new Avatar(profile.getNameProfile());
        H4 h4 = new H4(profile.getNameProfile());
        vl.add(avatar, h4);
        if (profile.isLock()) {
            vl.add(LineAwesomeIcon.LOCK_SOLID.create());
        }
        vl.addClickListener(e -> {
            if (profile.isLock()) {
                PasswordField passwordField = new PasswordField();
                passwordField.setPrefixComponent(VaadinIcon.LOCK.create());
                passwordField.setRequiredIndicatorVisible(true);
                passwordField.setRequired(true);
                passwordField.setAllowedCharPattern("[0-9]");
                passwordField.focus();
                passwordField.setWidthFull();

                Span passfailed = new Span();
                passfailed.addClassNames(LumoUtility.TextColor.ERROR);
                passfailed.addClassName("password-failed");


                Dialog passdialog = new Dialog(profile.getNameProfile());
                Button btnContinue = new Button("Continuar");

                passwordField.addInputListener(key -> {
                    passfailed.setText("");
                    if (key.equals(Key.ENTER)) {
                        btnContinue.click();
                    }
                });

                btnContinue.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                btnContinue.addClickListener(event -> {
                    if (profile.getPin().equalsIgnoreCase(passwordField.getValue())) {
                        continueToMainLayout(profile);
                        passdialog.close();
                    } else {
                        passfailed.setText("PIN incorrecto");
                    }

                });
                btnContinue.addClickShortcut(Key.ENTER);

                //Aviso
                Span spanaviso = new Span("Si olvidaste tu PIN, comunicate con el administrador");
                spanaviso.addClassName("span-aviso");
                spanaviso.getElement().getThemeList().add("badge warning");

                passdialog.setHeaderTitle(profile.getNameProfile());
                passdialog.add(new VerticalLayout(spanaviso, passwordField, passfailed));
                passdialog.getFooter().add(btnContinue);
                passdialog.open();
            } else {
                continueToMainLayout(profile);
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
