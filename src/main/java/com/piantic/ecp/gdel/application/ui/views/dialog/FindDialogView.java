package com.piantic.ecp.gdel.application.ui.views.dialog;

import com.piantic.ecp.gdel.application.backend.utils.generics.GenericService;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.lang.reflect.Method;
import java.util.List;

public class FindDialogView<T> extends Dialog {

    TextField txtFilter = new TextField();
    Grid<T> grid;
    GenericService<T> findService;
    Long idSelected;
    String searchField;
    Boolean redirect;
    String routerLink;

    List<String> namesColumnFilter;
    List<String> labeslColumnFilter;


    public FindDialogView(GenericService<T> findService, Class<T> entityClass, String searchField) {
        addClassName("find-dialog");
        this.findService = findService;
        this.grid = new Grid<>(entityClass);
        this.searchField = searchField;

        Button btnCloseDialog = new Button(LineAwesomeIcon.TIMES_CIRCLE.create(), e -> this.close());
        btnCloseDialog.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);
        getHeader().add(btnCloseDialog);
        setResizable(true);

        txtFilter.setPlaceholder("Buscar...");
        txtFilter.addClassName("filter-text");
        txtFilter.setValueChangeMode(ValueChangeMode.LAZY);
        txtFilter.addValueChangeListener(e -> findValue());
        txtFilter.setClearButtonVisible(true);

//        configureGrid();

        VerticalLayout vlayout = new VerticalLayout(txtFilter, grid);
        vlayout.addClassName("content-dialog");
        vlayout.setSizeFull();
        add(vlayout);

    }

    public void configureGrid() {
        grid.addClassName("grid-dialog");
        findValue();

        // Asume que todos los objetos T tienen un método getName
        grid.addColumn(new ComponentRenderer<>(item -> {
            String name = "";
            try {
                Method getNameMethod = item.getClass().getMethod("getName");
                name = (String) getNameMethod.invoke(item);
            } catch (Exception e) {
                //e.printStackTrace();
            }
            if (name != null && !name.isEmpty()) {
                return new Avatar(name);
            }
            return new Avatar("OK");
        })).setAutoWidth(true);

        //Establece las columnas enviadas en la lista (deben coincidir con el Entity)
        for (int i = 0; i < namesColumnFilter.size(); i++) {
            grid.setColumns(namesColumnFilter.get(i));
            //Primera Columna
            if(i==0){
                grid.getColumnByKey(namesColumnFilter.get(i)).setFlexGrow(1);
            }
            //Labels
            if(labeslColumnFilter.size()==namesColumnFilter.size())
                grid.getColumnByKey(namesColumnFilter.get(i)).setHeader(labeslColumnFilter.get(i));

        }


        grid.asSingleSelect().addValueChangeListener(e -> selectedData(findService.getId(e.getValue())));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
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

    public List<String> getNamesColumnFilter() {
        return namesColumnFilter;
    }

    public void setNamesColumnFilter(List<String> namesColumnFilter) {
        this.namesColumnFilter = namesColumnFilter;
    }

    public List<String> getLabeslColumnFilter() {
        return labeslColumnFilter;
    }

    public void setLabeslColumnFilter(List<String> labeslColumnFilter) {
        this.labeslColumnFilter = labeslColumnFilter;
    }
}
