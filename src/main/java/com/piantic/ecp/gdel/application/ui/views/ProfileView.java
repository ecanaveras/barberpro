package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.service.CustomerService;
import com.piantic.ecp.gdel.application.backend.service.ProfileService;
import com.piantic.ecp.gdel.application.ui.views.forms.ProfileForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIcon;

@PageTitle("Perfiles | BarberPro")
@Route("profiles")
public class ProfileView extends VerticalLayout {

    private ProfileForm profileForm;
    private final ProfileService profileService;
    Grid<Profile> grid = new Grid<>(Profile.class);
    TextField filterText = new TextField();
    Div content = new Div();

    public ProfileView(ProfileService profileService, CustomerService customerService) {
        this.profileService = profileService;
        addClassName("profile-view");
        setSizeFull();

        configureGrid();

        profileForm = new ProfileForm();
        //Events
        profileForm.addListener(ProfileForm.SaveEvent.class, this::saveProfile);
        profileForm.addListener(ProfileForm.DeleteEvent.class, this::deleteProfile);
        profileForm.addListener(ProfileForm.CloseEvent.class, e -> closeEditor());

        content.addClassName("content-div");
        content.add(grid, profileForm);
        content.setSizeFull();

        add(configureToolbar(), content);
        updateList();
        closeEditor();
    }

    //View
    private HorizontalLayout configureToolbar() {
        filterText.setClearButtonVisible(true);
        filterText.setPlaceholder("Buscar...");
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(event -> updateList());

        Button btnAdd = new Button(LineAwesomeIcon.PLUS_SOLID.create(), e -> addProfile());


        HorizontalLayout toolbar = new HorizontalLayout(filterText, btnAdd, new Button(LineAwesomeIcon.ENVELOPE_OPEN.create(), e -> {
        }));
//        HorizontalLayout toolbar = new HorizontalLayout(filterText, btnAdd, new Button(LineAwesomeIcon.ENVELOPE_OPEN.create(), e -> openFindDialog()));
        toolbar.addClassName("toolbar");
        return toolbar;

    }

    /*
    private void openFindDialog() {
        //Dialog
        FindDialogView dialogView = new FindDialogView(customerService);
        dialogView.addOpenedChangeListener(listener -> {
            if (!listener.isOpened()) {
                Notification.show("Id seleccionado: " + dialogView.getIdSelected());
            }
        });
        dialogView.open();
    }*/

    private void configureGrid() {
        grid.addClassName("profile-grid");
        grid.setSizeFull();
        grid.setColumns("nameProfile", "status");
        grid.addColumn(profile -> {
            return profile == null ? false : profile.isLock();
        }).setHeader("PIN");
        grid.addComponentColumn(new ValueProvider<Profile, Component>() {
            @Override
            public Component apply(Profile profile) {
                PasswordField password = new PasswordField();
                password.setEnabled(false);
                if (profile.isLock())
                    password.setValue(profile.getPin().toString());
                Checkbox checkbox = new Checkbox(profile.isLock());
                checkbox.setEnabled(false);
                return new HorizontalLayout(checkbox, password);
            }
        }).setHeader("Requiere PIN");
        //grid.getColumnByKey("lock").setHeader("Estado");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(click -> updateProfileForm(click.getValue()));
    }

    //Data

    private void addProfile() {
        profileForm.setVisible(true);
        profileForm.setProfile(new Profile());
        profileForm.addClassName("visible");
        addClassName("editing");

    }

    private void saveProfile(ProfileForm.SaveEvent evt) {
        //profileService.save((Profile) evt.getData());
        profileService.save((Profile) evt.getProfile());
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
        notification.show("Perfil Guardado");
        updateList();
        closeEditor();
    }

    private void deleteProfile(ProfileForm.DeleteEvent evt) {
        ConfirmDialog confirmDialog = new ConfirmDialog("Eliminar a \"" + evt.getProfile().getNameProfile() + "\"?",
                "¿Desea borrar el registro?",
                "Eliminar", e -> {
            profileService.delete(evt.getProfile());
            updateList();
            closeEditor();
        }, "Cancelar", e -> e.getSource().close());
        confirmDialog.setConfirmButtonTheme(ButtonVariant.LUMO_ERROR.getVariantName() + " " + ButtonVariant.LUMO_PRIMARY.getVariantName());
        confirmDialog.setCloseOnEsc(true);
        confirmDialog.open();

    }


    private void updateProfileForm(Profile profile) {
        if (profile != null) {
            profileForm.setProfile(grid.asSingleSelect().getValue());
            profileForm.setVisible(true);
            profileForm.addClassName("visible");
            addClassName("editing");
        } else {
            closeEditor();
        }
    }

    private void updateList() {
        grid.setItems(profileService.findAll(filterText.getValue()));
    }

    private void closeEditor() {
        profileForm.setProfile(null);
        profileForm.setVisible(false);
        profileForm.removeClassName("visible");
        removeClassName("editing");
    }
}
