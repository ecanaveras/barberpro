package com.piantic.ecp.gdel.application.ui.views.forms;

import com.piantic.ecp.gdel.application.backend.entity.Work;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class WorkForm extends Dialog {

    FormLayout formLayout = new FormLayout();
    TextField title = new TextField("Servicio");
    TextField description = new TextField("Descripción");
    NumberField price = new NumberField("Precio");
    NumberField commissions = new NumberField("Comisión");
    TextArea observations = new TextArea("Observaciones");


    Button save = new Button("Guardar");
    Button cancel = new Button("Cancelar");

    Binder<Work> binder = new BeanValidationBinder<>(Work.class);

    public WorkForm() {
        addClassName("work-form");
        binder.bindInstanceFields(this);

        save.setEnabled(false);

        //Textfields
        title.setPrefixComponent(LineAwesomeIcon.MAGIC_SOLID.create());
        title.setAutofocus(true);

        price.setPrefixComponent(LineAwesomeIcon.DOLLAR_SIGN_SOLID.create());
        price.setAllowedCharPattern("[0-9,.]");
        price.setMin(0);
        price.setValue(0.0);
        price.setAutoselect(true);

        commissions.setPrefixComponent(LineAwesomeIcon.HANDSHAKE.create());
        //commissions.setPattern("^([1-9]\\d*(\\.|\\,)\\d*|0?(\\.|\\,)\\d*[1-9]\\d*|[1-9]\\d*)$");
        commissions.setAllowedCharPattern("[0-9,.]");
        commissions.setMin(0);
        commissions.setMax(100);
        commissions.setStep(0.1);
        commissions.setValue(0.0);
        commissions.setSuffixComponent(LineAwesomeIcon.PERCENT_SOLID.create());
        commissions.setAutoselect(true);


        //Simulations
        TextField ganancia = new TextField("Ganancia Final");
        ganancia.setEnabled(false);
        TextField comision = new TextField("Comisión Final");
        comision.setEnabled(false);

        commissions.setValueChangeMode(ValueChangeMode.LAZY);
        commissions.addClientValidatedEventListener(e -> {
           if(e.isValid()){
               ganancia.setValue(String.valueOf(binder.getBean().getPrice() - (binder.getBean().getPrice() * binder.getBean().getCommissions() / 100)));
               comision.setValue(String.valueOf(binder.getBean().getPrice() * binder.getBean().getCommissions() / 100));
           }
        });


        //Form
        formLayout.add(title, description, price, commissions, ganancia, comision, observations);
        formLayout.setColspan(title, 2);
        formLayout.setColspan(description, 2);
        formLayout.setColspan(price, 1);
        formLayout.setColspan(commissions, 1);
        formLayout.setColspan(ganancia, 1);
        formLayout.setColspan(comision, 1);
        formLayout.setColspan(observations, 2);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));

        setButtons();
        HorizontalLayout btns = new HorizontalLayout(save, cancel);
        btns.setWidthFull();

        setHeaderTitle("Nuevo Servicio");
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
        }); //fireEvent(new WorkForm().CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
    }


    public void setWork(Work work) {
        binder.setBean(work);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new WorkForm.SaveEvent(this, binder.getBean()));
        }else {
            System.out.println("Binder no valid");
        }
    }

    //Events
    public static abstract class WorkFormEvent extends ComponentEvent<WorkForm> {

        private Work work;

        protected WorkFormEvent(WorkForm source, Work work) {
            super(source, false);
            this.work = work;
        }

        public Work getWork() {
            return work;
        }
    }

    public static class SaveEvent extends WorkForm.WorkFormEvent {
        public SaveEvent(WorkForm source, Work work) {
            super(source, work);
        }
    }

    public static class DeleteEvent extends WorkForm.WorkFormEvent {
        public DeleteEvent(WorkForm source, Work work) {
            super(source, work);
        }
    }

    public static class CloseEvent extends WorkForm.WorkFormEvent {
        public CloseEvent(WorkForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> event, ComponentEventListener<T> listener) {
        return getEventBus().addListener(event, listener);
    }

}
