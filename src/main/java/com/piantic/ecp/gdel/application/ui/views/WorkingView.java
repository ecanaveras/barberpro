package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.backend.entity.Work;
import com.piantic.ecp.gdel.application.backend.service.WorkService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import jakarta.validation.constraints.NotNull;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.text.DecimalFormat;
import java.util.stream.Collectors;

@Route("working")
public class WorkingView extends Div {

    Grid<WorkAdded> gridservices = new Grid<>(WorkAdded.class, false);
    Grid<WorkAdded> gridsales = new Grid<>(WorkAdded.class, false);
    WorkService workService;
    Div divleft = new Div();
    Div divright = new Div();
    Div divfooter = new Div();
    Span total = new Span();

    WorkAdded dragitem;
    WorkAdded workAdded;

    GridListDataView<WorkAdded> dataviewleft = gridservices.getListDataView();
    GridListDataView<WorkAdded> dataviewright = gridsales.getListDataView();

    public WorkingView(WorkService workService) {
        addClassName("working-view");

        this.workService = workService;

        divleft.addClassName("div-left");
        divright.addClassName("div-right");

        configureGrids();

        divleft.add(gridservices);

        divright.add(gridsales);

        divfooter.add(new H3("Total: $"), total);

        add(divleft, divright, divfooter);
    }


    private void configureGrids() {
        //Disponibles
        gridservices.setItems(workService.findAll("").stream().map(work -> {
            WorkAdded service = new WorkAdded();
            service.setServicio(work);
            return service;
        }).collect(Collectors.toList()));
        gridservices.setRowsDraggable(true);
        gridservices.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        gridservices.addComponentColumn(service -> {
            Div itemservice = new Div();
            itemservice.addClassName("item-service");
            return getIconItem(service.getServicio(), itemservice);
        });
        gridservices.addComponentColumn(service -> {
            return service.isAdded() ? markIcon() : null;
        }).setAutoWidth(true).setFlexGrow(0);

        //Double Touch
        gridservices.addItemDoubleClickListener(e -> {
            dragitem = e.getItem();
            addServiceGrid();
        });


        //Drag and Drog
        gridservices.addDragStartListener(e -> {
            dragitem = e.getDraggedItems().get(0);
            gridservices.setDropMode(GridDropMode.ON_GRID);
            gridsales.setDropMode(GridDropMode.ON_GRID);
        });
        gridservices.addDragEndListener(e -> {
            dragitem = null;
            gridservices.setDropMode(null);
            gridsales.setDropMode(null);
            updateFooter();
        });


        //Vendidos
        gridsales.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        gridsales.setRowsDraggable(true);
        gridsales.addComponentColumn(service -> {
            Div itemservice = new Div();
            itemservice.addClassNames("item-service", "added");
            return getIconItem(service.getServicio(), itemservice);
        });
        gridsales.addComponentColumn(service -> {
            return createForm(service);
        }).setAutoWidth(true).setFlexGrow(0);
        gridsales.addColumn(
                new ComponentRenderer<>(Button::new, (btn, work_item) -> {
                    btn.addClassName("btn-delete-item");
                    btn.setIcon(LineAwesomeIcon.TRASH_ALT.create());
                    btn.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
                    //Elimina el elemento
                    btn.addClickListener(e -> {
                        dragitem = work_item;
                        dragitem.setAdded(false);
                        dragitem.setCant(0);
                        dataviewright.removeItem(dragitem);
                        dataviewleft.refreshItem(dragitem);
                        dragitem = null;
                        dataviewleft.refreshAll();
                        updateFooter();
                    });
                })
        ).setAutoWidth(true).setFlexGrow(0);


        gridsales.addDropListener(e -> {
            addServiceGrid();
        });


    }

    private void addServiceGrid() {
        dragitem.setAdded(true);
        dragitem.setCant(dragitem.getCant() + 1);
        dataviewleft.refreshItem(dragitem);
        dataviewright.addItem(dragitem);
        updateFooter();
        dragitem = null;
    }


    private void updateFooter() {
        Double val = gridsales.getListDataView()
                .getItems()
                .toList()
                .stream()
                .mapToDouble(item -> item.subTotal())
                .sum();
        total.setText(formatNumber(val));

    }

    @NotNull
    private Div getIconItem(Work work, Div itemservice) {
        Div div = new Div();
        div.addClassName("item-service-avatar");
        if (work.getImage() != null && !work.getImage().isEmpty()) {
            div.add(new SvgIcon("line-awesome/svg/" + work.getImage() + ".svg"));
        } else {
            Avatar avatar = new Avatar();
            avatar.addClassName("avatar-service");
            avatar.setName(work.getTitle());
            div.add(avatar);
        }
        itemservice.add(div);
        Div div2 = new Div();
        H5 h5 = new H5(work.getTitle());
        Span price = new Span(formatNumber(work.getPrice()));
        price.getElement().getThemeList().add("badge contrast");
        div2.add(h5, price);
        itemservice.add(div2);
        return itemservice;
    }


    private Icon markIcon() {
        Icon icon = VaadinIcon.CHECK.create();
        icon.addClassName("check-service");
        icon.getElement().getThemeList().add("badge success");
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        // Accessible label
        icon.getElement().setAttribute("aria-label", "Agregado");
        // Tooltip
        icon.getElement().setAttribute("title", "Agregado");
        return icon;
    }

    private Component createForm(WorkAdded work) {
        Div div = new Div();
        div.addClassName("mini-form");
        Button minus = new Button(VaadinIcon.MINUS.create());
        Button plus = new Button(VaadinIcon.PLUS.create());
        NumberField cant = new NumberField();
        cant.setStep(1);
        cant.setValue(Double.valueOf(work.cant));
        div.add(minus, cant, plus);
        return div;
    }

    private String formatNumber(Double number) {
        return new DecimalFormat("#,###.##").format(number);
    }


    //Class
    private class WorkAdded {
        private Work servicio;
        private int cant;
        private boolean added;

        public Work getServicio() {
            return servicio;
        }

        public void setServicio(Work servicio) {
            this.servicio = servicio;
        }

        public int getCant() {
            return cant;
        }

        public void setCant(int cant) {
            this.cant = cant;
        }

        public Double subTotal() {
            return cant * getServicio().getPrice();
        }

        public boolean isAdded() {
            return added;
        }

        public void setAdded(boolean added) {
            this.added = added;
        }
    }

}
