package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.Application;
import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.entity.Role;
import com.piantic.ecp.gdel.application.backend.service.ProfileService;
import com.piantic.ecp.gdel.application.backend.service.RoleService;
import com.piantic.ecp.gdel.application.backend.utils.NotificationUtil;
import com.piantic.ecp.gdel.application.ui.views.specials.HomeView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.HashSet;
import java.util.Set;

@PageTitle("Bienvenido | BarberPro")
@Route("welcome")
public class WelcomeView extends Div {

    private ProfileService profileService;
    private RoleService roleService;

    public WelcomeView(ProfileService profileService, RoleService roleService) {
        addClassName("welcome-view");
        this.profileService = profileService;
        this.roleService = roleService;

        Div divtitle = new Div();
        H3 welcome = new H3("Bienvenido a BarberPro");
        H5 h5 = new H5("Selecciona tu Perfil para continuar");

        Div divcontent = new Div(welcome);
        divcontent.addClassName("welcome-content");

        Div divcards = new Div();
        divcards.addClassName("welcome-cards");

        profileService.findAll().forEach(profile -> {
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
            divcontent.add(getUINewProfile());
            add(divcontent);
        }


    }

    private Component getUINewProfile() {
        Div divnewprofile = new Div();
        divnewprofile.addClassName("welcome-newprofile");
        H5 h5 = new H5("Crea un PERFIL para continuar");
        h5.addClassName(LumoUtility.TextColor.PRIMARY);

        Button btnContinue = new Button("Continuar");

        //Inputs
        TextField nameProfile = new TextField();
        nameProfile.focus();
        nameProfile.setPlaceholder("Nombre del Peril");
        nameProfile.setRequired(true);
        nameProfile.setValueChangeMode(ValueChangeMode.LAZY);
        nameProfile.addValueChangeListener(listener -> {
            if (!nameProfile.getValue().isEmpty() && nameProfile.getValue().toString().length() > 3) {
                btnContinue.setEnabled(true);
            }
        });


        FormLayout formLayout = new FormLayout();
        //Form
        formLayout.add(nameProfile);
        formLayout.setColspan(nameProfile, 2);

        btnContinue.setEnabled(false);
        btnContinue.addClickShortcut(Key.ENTER);
        btnContinue.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnContinue.addClickListener(e -> {
            if (nameProfile.getValue().isEmpty()) {
                NotificationUtil.showWarning("Perfil no válido");
                return;
            }

            Profile profile = new Profile();
            profile.setNameProfile(nameProfile.getValue());
            profile.setStatus(Profile.Status.Activo);

            //Agregar ROLE_ADMINISTRADOR sino existe
            Role role = new Role("ROLE_ADMINISTRADOR");
            Set<Role> roles = new HashSet<>(roleService.findAll(role.getName()));

            profileService.save(profile);

            //Asiga el PERFIL al ROLE
            if (!roles.isEmpty()) {
                //Actualizar el ROLE Existente
                role = roles.iterator().next();
            }
            role.setProfiles(new HashSet<>(profileService.findAll(profile.getNameProfile())));
            roleService.save(role);

            continueToMainLayout(profile);
        });

        divnewprofile.add(h5, formLayout, btnContinue);

        return divnewprofile;
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
