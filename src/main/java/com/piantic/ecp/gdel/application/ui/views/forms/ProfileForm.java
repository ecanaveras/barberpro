package com.piantic.ecp.gdel.application.ui.views.forms;

import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.utils.generics.FormEvents;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class ProfileForm extends FormLayout {

    TextField nameProfile = new TextField("Nombre del Perfil");
    ComboBox<Profile.Status> status = new ComboBox<>("Estado");
    PasswordField pin = new PasswordField("PIN");

    Button save = new Button("Guardar");
    Button cancel = new Button("Cancelar");
    Button delete = new Button();

    Binder<Profile> binder = new BeanValidationBinder<>(Profile.class);

    public ProfileForm(){
        addClassName("profile-form");

        binder.bindInstanceFields(this);
        status.setItems(Profile.Status.values());

        add(nameProfile, status, pin);
        setButtons();
        HorizontalLayout btns = new HorizontalLayout(save, cancel, delete);
        btns.setWidthFull();

        add(btns);
    }

    private void setButtons(){
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.setIcon(LineAwesomeIcon.TRASH_ALT.create());
        delete.addClassName("btn-delete");

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        cancel.addClickListener(e -> setVisible(false));

        save.addClickListener(e -> validateAndSave());
        delete.addClickListener(e -> fireEvent(new FormEvents.SaveEvent(this, binder.getBean())));
        cancel.addClickListener(e -> fireEvent(new FormEvents.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
    }

    public void setProfile(Profile profile){
        binder.setBean(profile);
    }

    private void validateAndSave(){
        if(binder.isValid()){
            fireEvent(new FormEvents.SaveEvent(this, binder.getBean()));
        }
    }

    //Events
    public static abstract class ProfileFormEvent extends ComponentEvent<ProfileForm>{

        private Profile profile;

        protected ProfileFormEvent(ProfileForm source, Profile profile) {
            super(source, false);
            this.profile=profile;
        }

        public Profile getProfile() {
            return profile;
        }
    }

    public static class SaveEvent extends ProfileFormEvent {
        public SaveEvent(ProfileForm source, Profile profile) {
            super(source, profile);
        }
    }

    public static class DeleteEvent extends ProfileFormEvent {
        public DeleteEvent(ProfileForm source, Profile profile) {
            super(source, profile);
        }
    }

    public static class CloseEvent extends ProfileFormEvent{
        public CloseEvent(ProfileForm source) {
            super(source, null);
        }
    }


    public <T extends ComponentEvent<?>> Registration addListener(Class<T> event, ComponentEventListener<T> listener){
        return getEventBus().addListener(event, listener);
    }

}
