package com.piantic.ecp.gdel.application.ui.views.forms;

import com.piantic.ecp.gdel.application.backend.entity.Customer;
import com.piantic.ecp.gdel.application.ui.views.specials.GenericForm;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import org.vaadin.lineawesome.LineAwesomeIcon;


public class CustomerForm extends GenericForm<Customer> {

    TextField name = new TextField("Nombre");
    TextField phone = new TextField("Télefono");
    EmailField email = new EmailField("Email");
    Checkbox favorite = new Checkbox("Favorito", false);

    public CustomerForm() {
        super();
        addClassName("customer-form");

        setHeaderTitle("Nuevo Cliente");

        binder.bindInstanceFields(this);
        binder.forField(favorite).bind(Customer::isFavorite, Customer::setFavorite);


        //Textfields
        name.setPrefixComponent(LineAwesomeIcon.USER_CIRCLE.create());
        name.setAutofocus(true);

        phone.setPrefixComponent(VaadinIcon.PHONE.create());
        phone.setPattern("-?[0-9]*");
        phone.setAllowedCharPattern("[0-9()+-]");

        email.setPrefixComponent(VaadinIcon.AT.create());


        //Form
        formLayout.add(name, email, phone, favorite);
        formLayout.setColspan(name, 2);
        formLayout.setColspan(phone, 1);
        formLayout.setColspan(favorite, 1);
        formLayout.setColspan(email, 2);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));

    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        if(getEntity() != null && getEntity().getId() != null) {
            setHeaderTitle("Editando Cliente");
        }
    }
}
