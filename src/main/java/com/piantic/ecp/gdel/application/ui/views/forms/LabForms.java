package com.piantic.ecp.gdel.application.ui.views.forms;

import com.piantic.ecp.gdel.application.backend.entity.Work;
import com.piantic.ecp.gdel.application.ui.views.specials.GenericForm;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class LabForms extends GenericForm<Work> {

    private TextField title = new TextField("Title");
    private TextField description = new TextField("Description");
    private NumberField price = new NumberField("Price");
    private NumberField commissions = new NumberField("Commissions");
    private TextArea observations = new TextArea("Observations");

    public LabForms() {
        super();
        binder.bindInstanceFields(this);

        title.setPrefixComponent(LineAwesomeIcon.MAGIC_SOLID.create());
        title.setAutofocus(true);

        price.setPrefixComponent(LineAwesomeIcon.DOLLAR_SIGN_SOLID.create());
        price.setAllowedCharPattern("[0-9,.]");
        price.setMin(0);
        price.setValue(0.0);
        price.setAutoselect(true);

        commissions.setPrefixComponent(LineAwesomeIcon.HANDSHAKE.create());
        commissions.setAllowedCharPattern("[0-9,.]");
        commissions.setMin(0);
        commissions.setMax(100);
        commissions.setStep(0.1);
        commissions.setValue(0.0);
        commissions.setSuffixComponent(LineAwesomeIcon.PERCENT_SOLID.create());
        commissions.setAutoselect(true);

        // Simulations
        TextField gain = new TextField("Final Gain");
        gain.setEnabled(false);
        TextField commission = new TextField("Final Commission");
        commission.setEnabled(false);

        commissions.setValueChangeMode(ValueChangeMode.LAZY);
        commissions.addClientValidatedEventListener(e -> {
            if (e.isValid()) {
                Work entity = binder.getBean();
                gain.setValue(String.valueOf(entity.getPrice() - (entity.getPrice() * entity.getCommissions() / 100)));
                commission.setValue(String.valueOf(entity.getPrice() * entity.getCommissions() / 100));
            }
        });

        // Form Layout
        formLayout.add(title, description, price, commissions, gain, commission, observations);
        formLayout.setColspan(title, 2);
        formLayout.setColspan(description, 2);
        formLayout.setColspan(price, 1);
        formLayout.setColspan(commissions, 1);
        formLayout.setColspan(gain, 1);
        formLayout.setColspan(commission, 1);
        formLayout.setColspan(observations, 2);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));

        setHeaderTitle("Nuevo Registro");
    }
}
