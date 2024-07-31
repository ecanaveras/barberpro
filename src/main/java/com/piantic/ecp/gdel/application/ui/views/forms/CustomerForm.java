package com.piantic.ecp.gdel.application.ui.views.forms;

import com.piantic.ecp.gdel.application.backend.entity.Customer;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.vaadin.lineawesome.LineAwesomeIcon;


public class CustomerForm extends Dialog {

    FormLayout formLayout = new FormLayout();
    TextField name = new TextField("Nombre");
    TextField phone = new TextField("Télefono");
    EmailField email = new EmailField("Email");
    Checkbox favorite = new Checkbox("Favorito", false);

    Button save = new Button("Guardar");
    Button cancel = new Button("Cancelar");

    Binder<Customer> binder = new BeanValidationBinder<>(Customer.class);

    public CustomerForm() {
        addClassName("customer-form");
        binder.bindInstanceFields(this);
        binder.forField(favorite).bind(Customer::isFavorite, Customer::setFavorite);

        save.setEnabled(false);

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

        setButtons();
        HorizontalLayout btns = new HorizontalLayout(save, cancel);
        btns.setWidthFull();

        setHeaderTitle("Nuevo Cliente");
        Button btnCloseDialog = new Button(LineAwesomeIcon.TIMES_CIRCLE.create(), e -> this.close());
        btnCloseDialog.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);
        getHeader().add(btnCloseDialog);
        add(formLayout);
        getFooter().add(btns);

    }

    private void setButtons() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        cancel.addClickShortcut(Key.ESCAPE);

        cancel.addClickListener(e -> this.close());

        save.addClickListener(e -> validateAndSave());
        cancel.addClickListener(e -> {
        }); //fireEvent(new CustomerForm().CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
    }


    public void setCustomer(Customer customer) {
        binder.setBean(customer);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new CustomerForm.SaveEvent(this, binder.getBean()));
        }else {
            System.out.println("Binder no valid");
        }
    }

    //Events
    public static abstract class CustomerFormEvent extends ComponentEvent<CustomerForm> {

        private Customer customer;

        protected CustomerFormEvent(CustomerForm source, Customer customer) {
            super(source, false);
            this.customer = customer;
        }

        public Customer getCustomer() {
            return customer;
        }
    }

    public static class SaveEvent extends CustomerForm.CustomerFormEvent {
        public SaveEvent(CustomerForm source, Customer customer) {
            super(source, customer);
        }
    }

    public static class DeleteEvent extends CustomerForm.CustomerFormEvent {
        public DeleteEvent(CustomerForm source, Customer profile) {
            super(source, profile);
        }
    }

    public static class CloseEvent extends CustomerForm.CustomerFormEvent {
        public CloseEvent(CustomerForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> event, ComponentEventListener<T> listener) {
        return getEventBus().addListener(event, listener);
    }

}
