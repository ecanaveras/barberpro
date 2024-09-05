package com.piantic.ecp.gdel.application.ui.views.forms;

import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.ui.views.specials.GenericForm;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class ProfileForm extends GenericForm<Profile> {

    TextField nameProfile = new TextField("Nombre del Perfil");
    TextField phone = new TextField("Telefono");
    TextField email = new TextField("Email");
    ComboBox<Profile.Status> status = new ComboBox<>("Estado");
    PasswordField pin = new PasswordField("PIN");

    private Profile updateitem;

    public ProfileForm() {
        super();
        addClassName("profile-form");

        setHeaderTitle("Nuevo Perfil");

        binder.bindInstanceFields(this);
//        configureGrids();

        //Textfields
        nameProfile.setPrefixComponent(LineAwesomeIcon.USER_CIRCLE.create());
        nameProfile.setAutofocus(true);

        phone.setPrefixComponent(VaadinIcon.PHONE.create());

        email.setPrefixComponent(VaadinIcon.MAILBOX.create());

        pin.setPrefixComponent(VaadinIcon.LOCK.create());
        pin.setPattern("[0-9]");
        pin.setAllowedCharPattern("[0-9]");

        status.setItems(Profile.Status.values());

        //Form
        formLayout.add(nameProfile, email, phone, status, pin);
        formLayout.setColspan(nameProfile, 2);
        formLayout.setColspan(email, 2);
        formLayout.setColspan(phone, 1);
        formLayout.setColspan(status, 1);
        formLayout.setColspan(pin, 1);
//        formLayout.add(new Span("Seleccione los roles para el perfil"), roles);
//        formLayout.setColspan(roles, 2);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));


    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        updateitem = this.getEntity();
    }
}
