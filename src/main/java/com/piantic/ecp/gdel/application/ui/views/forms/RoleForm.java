package com.piantic.ecp.gdel.application.ui.views.forms;

import com.piantic.ecp.gdel.application.backend.entity.Role;
import com.piantic.ecp.gdel.application.backend.entity.Work;
import com.piantic.ecp.gdel.application.backend.service.WorkService;
import com.piantic.ecp.gdel.application.ui.views.specials.GenericForm;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.ArrayList;


public class RoleForm extends GenericForm<Role> {

    private WorkService workService;
    private Role updateitem;

    TextField name = new TextField("Nombre del Role");

    Grid<Work> attacths = new Grid<>(Work.class, false);

    public RoleForm(WorkService workService) {
        super();
        addClassName("role-form");
        this.workService = workService;

        setHeaderTitle("Nuevo Role");

        binder.bindInstanceFields(this);
        configureGrids();

        //Textfields
        name.setPrefixComponent(LineAwesomeIcon.OBJECT_GROUP.create());
        name.setAutofocus(true);

        //Form
//        formLayout.add(name, avalaibles, attacths);
        formLayout.add(name, new Span("Seleccione los servicios para el ROLE"), attacths);
//        formLayout.setColspan(name, 2);
//        formLayout.setColspan(attacths, 2);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        if (updateitem != null) {
            binder.readBean(updateitem);
            updateitem.getWorks().forEach(work -> attacths.select(work));
        }
    }


    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        updateitem = this.getEntity();
        binder.readBean(updateitem);
        updateitem.getWorks().forEach(work -> attacths.select(work));
    }


    private void configureGrids() {
        attacths.setItems(new ArrayList<>());
        attacths.setItems(workService.findAll());
        attacths.setSelectionMode(Grid.SelectionMode.MULTI);
        attacths.addColumn("title").setHeader("Servicios");
        attacths.addSelectionListener(listener -> {
            workSet = attacths.getSelectedItems();
        });
    }



}
