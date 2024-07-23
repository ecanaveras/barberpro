package com.piantic.ecp.gdel.application.ui.views.forms;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class ActivityDetailForm extends Div {

    FormLayout formLayout = new FormLayout();
    String ID;

    public ActivityDetailForm() {
        configureView(null);
    }

    public ActivityDetailForm(String id) {
        configureView(id);
    }

    private void configureView(String id){
        addClassNames("slide-in-div");
        this.ID = id;

        configureForm();
        configureView();
    }

    private void configureView() {
        addClassNames(LumoUtility.BoxShadow.MEDIUM);

        HorizontalLayout header = new HorizontalLayout();
        Span title = new Span("Detalles " + this.getID());
        Span state = new Span("Por aprobar");
        state.getElement().getThemeList().add("bagde contrast");
        Button btnClose = new Button(LineAwesomeIcon.TIMES_CIRCLE.create(), buttonClickEvent -> this.removeClassName("visible"));
        btnClose.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);
        btnClose.addClassName(LumoUtility.Margin.Left.AUTO);
        btnClose.addClickShortcut(Key.ESCAPE);
        header.add(title, state, btnClose);

        VerticalLayout layout = new VerticalLayout();
        layout.add(header, formLayout);
        add(layout);
    }


    private void configureForm() {
        TabSheet tabSheet = new TabSheet();
        tabSheet.setWidthFull();
        tabSheet.add("Tab", new HorizontalLayout(new Span("Hola Mundo")));
        tabSheet.add("Tab", new HorizontalLayout(new Span("Otro Dato")));
        formLayout.add(tabSheet);

    }

    public void updateUI(){
        removeAll();
        formLayout = new FormLayout();
        this.configureView(this.getID());
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
