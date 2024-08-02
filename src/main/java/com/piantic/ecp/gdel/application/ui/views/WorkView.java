package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.backend.entity.Work;
import com.piantic.ecp.gdel.application.backend.service.WorkService;
import com.piantic.ecp.gdel.application.ui.views.forms.WorkForm;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.text.DecimalFormat;

@PageTitle("Servicios | BarberPro")
@Route("service")
public class WorkView extends HorizontalLayout implements HasUrlParameter<Long> {

    private final TextField txtFilter;
    private final Button btnAdd;
    private final Span count = new Span();
    private Div contentRight;
    private VerticalLayout contentLeft;
    //private WordS customerviewdetail;
    private Grid<Work> grid = new Grid<>(Work.class, false);
    private WorkService workService;
    private Boolean detailAdded = false;

    public WorkView(WorkService workService) {
        addClassName("work-view");
        setSizeFull();


        this.workService = workService;

        //Title
        H3 title = new H3("Servicios");
        //title.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.FontSize.MEDIUM);
        count.addClassNames(LumoUtility.FontWeight.LIGHT);
        count.getElement().getThemeList().add("badge");
        HorizontalLayout contentTitle = new HorizontalLayout(title, count);
        contentTitle.setAlignSelf(Alignment.BASELINE);

        //Toolbar
        txtFilter = new TextField();
        txtFilter.setClearButtonVisible(true);
        txtFilter.setPlaceholder("Filtrar...");
        txtFilter.setValueChangeMode(ValueChangeMode.LAZY);
        txtFilter.addValueChangeListener(e -> updateList());
        txtFilter.addClassNames(LumoUtility.Flex.AUTO);
        txtFilter.setMaxWidth("26rem");

        btnAdd = new Button(LineAwesomeIcon.MAGIC_SOLID.create());
        btnAdd.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnAdd.setTooltipText("Agregar...");
        btnAdd.addClickListener(click -> {
            openFormDialog(new Work());
        });

        HorizontalLayout toolbar = new HorizontalLayout(txtFilter, btnAdd);
        toolbar.setWidthFull();
        toolbar.setFlexGrow(0);

        //Grid
        configureGrid();

        updateList();

        //Content Left
        contentLeft = new VerticalLayout();
        contentLeft.addClassName("content-left");
        contentLeft.setSizeFull();

        contentLeft.add(contentTitle, toolbar, grid);

        //Content Right
        contentRight = new Div();
        contentRight.addClassName("content-right");

        add(contentLeft);
        add(contentRight);

        System.out.println("Reloadedddd");
    }

    private void openFormDialog(Work work) {
        WorkForm workForm = new WorkForm();
        workForm.setEntity(work);
        workForm.setSaveEventListener(e -> {
            this.saveWork(e);
            workForm.close();
        });
        workForm.open();
    }

    private void configureGrid() {
        grid.addClassName("work-grid");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(work -> {
            HorizontalLayout content = new HorizontalLayout();
            content.setAlignItems(Alignment.CENTER);
            VerticalLayout layout = new VerticalLayout();
            layout.addClassName("work-grid-service");
            layout.add(new H5(work.getTitle()),new Span(work.getDescription()));
            content.add(new Avatar(work.getTitle()), layout);
           return content;
        }).setAutoWidth(true).setHeader("Servicio").setSortable(true).setComparator(Work::getTitle).getStyle().set("min-width", "200px");

        grid.addComponentColumn(work -> {
            Span span = new Span();
            span.setText(new DecimalFormat("$ #,###.##").format(work.getPrice()));
            span.getElement().getThemeList().add("badge success");
            return span;
        }).setHeader("Precio").setComparator(Work::getPrice);

        grid.addComponentColumn(work -> {
            Span span = new Span();
            span.setText(new DecimalFormat("###.##").format(work.getCommissions())+" %");
            span.getElement().getThemeList().add("badge");
            return span;
        }).setHeader("Comisión").setComparator(Work::getCommissions);

        grid.addColumn("observations").setHeader("Observaciones");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        //grid.addColumns("price", "commissions", "description", "observations");
//        grid.addColumn(Customer::getPhone).setHeader("Teléfono").setSortable(true);
//        grid.addColumn(Customer::getEmail).setHeader("Email").setSortable(true);
//        grid.addComponentColumn(customer -> {
//            SvgIcon star = LineAwesomeIcon.STAR_SOLID.create();
//            star.addClassName("star-icon");
//            return customer != null && customer.isFavorite() ? star : null;
//        }).setHeader("Favorito").setWidth("6rem");

        createMenu();

        grid.asSingleSelect().addValueChangeListener(e -> showDetail(e.getValue() != null ? e.getValue().getId() : null));
    }

    private void updateList() {
        grid.setItems(workService.findAll(txtFilter.getValue()));
        count.setText(String.valueOf(workService.count()));
    }

    public void saveWork(Work work) {
        workService.save(work);
        updateList();
        Notification.show("Servicio Guardado!");
    }

    private void deleteWork(Work work) {
        ConfirmDialog confirmDialog = new ConfirmDialog("¿Eliminar a \"" + work.getTitle() + "\"?",
                "¿Desea borrar el registro?",
                "Eliminar", e -> {
            workService.delete(work);
            updateList();
            getUI().ifPresent(ui -> ui.navigate(WorkView.class));
        }, "Cancelar", e -> e.getSource().close());
        confirmDialog.setConfirmButtonTheme(ButtonVariant.LUMO_ERROR.getVariantName() + " " + ButtonVariant.LUMO_PRIMARY.getVariantName());
        confirmDialog.setCloseOnEsc(true);
        confirmDialog.open();

    }

    private void createMenu() {
        GridContextMenu<Work> menu = grid.addContextMenu();
        Span new1 = new Span("Nuevo");
        new1.setClassName(LumoUtility.TextColor.PRIMARY);
        menu.addItem(new HorizontalLayout(new1, LineAwesomeIcon.MAGIC_SOLID.create()), e -> openFormDialog(new Work()));
        menu.addItem("Editar", e -> openFormDialog(e.getItem().get()));
        menu.addItem("Detalles", e -> showDetail(e.getItem().get().getId()));
        menu.add(new Hr());
        GridMenuItem itemDel = menu.addItem("Eliminar", e -> deleteWork(e.getItem().get()));
        itemDel.addClassNames(LumoUtility.TextColor.ERROR);
    }

    private void showDetail(Long id) {
        if (id != null) {
            getUI().ifPresent(ui -> ui.navigate(WorkView.class, id));
        } else {
            getUI().ifPresent(ui -> ui.navigate(WorkView.class));
        }
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long id) {
      /*  if (id != null) {

            if (workService.findByCustomerId(id) == null) {
//                beforeEvent.rerouteToError(IllegalArgumentException.class, getTranslation("customer.not.found", beforeEvent.getLocation().getPath()));
//                beforeEvent.forwardTo("error");
                Notification noti = new Notification();
                noti.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
                Div text = new Div(new Text("Registro no encontrado..."));
                Button closeBtn = new Button(new Icon("lumo", "cross"));
                closeBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
                closeBtn.setAriaLabel("Cerrar");
                closeBtn.addClickListener(e -> {
                    noti.close();
                });
                HorizontalLayout layout = new HorizontalLayout(text, closeBtn);
                layout.setAlignSelf(Alignment.CENTER);

                noti.add(layout);
                noti.setPosition(Notification.Position.MIDDLE);
                noti.open();
                return;
            }
            if (customerviewdetail != null && customerviewdetail.getCustomer() != null && customerviewdetail.getCustomer().getId().equals(id)) {
                if(contentRight.getClassNames().contains("visible")) {
                    contentLeft.removeClassName("viewing");
                    contentRight.removeClassName("visible");
                    return;
                }
            }
            if (customerviewdetail == null) {
                customerviewdetail = new CustomerViewDetail(workService, id);
                contentRight.add(customerviewdetail);
            } else {
                customerviewdetail.updateUI(id);
            }
            contentRight.addClassName("visible");
            contentLeft.addClassName("viewing");


        } else {
            if (customerviewdetail != null) {
                contentLeft.removeClassName("viewing");
                contentRight.removeClassName("visible");
            }
        }*/
    }

}
