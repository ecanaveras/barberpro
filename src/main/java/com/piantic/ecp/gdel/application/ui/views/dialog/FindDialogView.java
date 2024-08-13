package com.piantic.ecp.gdel.application.ui.views.dialog;

import com.piantic.ecp.gdel.application.backend.utils.generics.GenericService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class FindDialogView<T> extends Dialog {

    TextField txtFilter = new TextField();
    Grid<T> grid;
    GenericService<T> findService;
    Long idSelected;
    String searchField;
    Boolean redirect;
    String routerLink;


    public FindDialogView(GenericService<T> findService, Class<T> entityClass, String searchField) {
        addClassName("find-dialog");
        this.findService = findService;
        this.grid = new Grid<>(entityClass);
        this.searchField = searchField;


        setMinWidth("400px");
        setMinHeight("400px");

        setHeaderTitle("Buscando...");
        Button btnCloseDialog = new Button(LineAwesomeIcon.TIMES_CIRCLE.create(), e -> this.close());
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
        findValue();
        grid.setColumns("name");
        grid.setSizeFull();
        grid.setMaxHeight("90%");
        grid.asSingleSelect().addValueChangeListener(e -> selectedData(findService.getId(e.getValue())));
    }

    private void findValue() {
        grid.setItems(findService.findAll(txtFilter.getValue()));
    }

    private void selectedData(Long id) {
        this.idSelected = id;
        this.close();
    }

    public Long getIdSelected() {
        return idSelected;
    }
}
