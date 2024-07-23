package com.piantic.ecp.gdel.application.ui.views.dialog;

import com.piantic.ecp.gdel.application.backend.entity.Customer;
import com.piantic.ecp.gdel.application.backend.service.CustomerService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class FindDialogView extends Dialog {

    TextField txtFilter = new TextField();
    Grid<Customer> grid = new Grid<>(Customer.class);
    CustomerService findService;
    Long idSelected;

    public FindDialogView(CustomerService findService) {
        addClassName("find-dialog");
        this.findService = findService;

        setWidth("600px");
        setHeight("800px");
        setHeaderTitle("Buscando...");
        Button btnCloseDialog = new Button(LineAwesomeIcon.TIMES_CIRCLE.create(), e-> this.close());
        btnCloseDialog.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);
        getHeader().add(btnCloseDialog);
        setResizable(true);

        txtFilter.setPlaceholder("Buscar...");
        txtFilter.setValueChangeMode(ValueChangeMode.LAZY);
        txtFilter.addValueChangeListener(e -> findValue());
        txtFilter.setClearButtonVisible(true);

        configureGrid();

        VerticalLayout vlayout = new VerticalLayout(txtFilter, grid);
        vlayout.setSizeFull();
        add(vlayout);

    }

    private void configureGrid() {
        grid.addClassName("grid-dialog");
        grid.setColumns("name");
        grid.setSizeFull();
        grid.setMaxHeight("90%");
        grid.asSingleSelect().addValueChangeListener(e-> selectedData(e.getValue().getId()));
        findValue();
    }

    private void findValue() {
        grid.setItems(findService.findAll(txtFilter.getValue()));
    }

    private void selectedData(Long id){
        this.idSelected = id;
        this.close();
        System.out.printf("Seleccionado: "+id);
    }

    public Long getIdSelected() {
        return idSelected;
    }
}
