package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.backend.entity.Customer;
import com.piantic.ecp.gdel.application.backend.service.CustomerService;
import com.piantic.ecp.gdel.application.backend.utils.NotificationUtil;
import com.piantic.ecp.gdel.application.ui.views.details.CustomerViewDetail;
import com.piantic.ecp.gdel.application.ui.views.forms.CustomerForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

@PageTitle("Clientes")
@Route(value = "customer", layout = MainLayout.class)
public class CustomerView extends HorizontalLayout implements HasUrlParameter<Long> {

    private final TextField txtFilter;
    private final Button btnAdd;
    private final Span count = new Span();
    private Div contentRight;
    private VerticalLayout contentLeft;
    private CustomerViewDetail customerviewdetail;
    private Grid<Customer> grid = new Grid<>(Customer.class, false);
    public CustomerService customerService;
    private Boolean detailAdded = false;

    public CustomerView(CustomerService customerService) {
        addClassName("customer-view");
        setSizeFull();


        this.customerService = customerService;

        //Toolbar
        txtFilter = new TextField();
        txtFilter.setClearButtonVisible(true);
        txtFilter.setPlaceholder("Filtrar...");
        txtFilter.setValueChangeMode(ValueChangeMode.LAZY);
        txtFilter.addValueChangeListener(e -> updateList());
        txtFilter.addClassNames(LumoUtility.Flex.AUTO);
        txtFilter.setMaxWidth("26rem");

        btnAdd = new Button(LineAwesomeIcon.USER_PLUS_SOLID.create());
        btnAdd.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnAdd.addClickListener(click -> {
            openFormDialog(new Customer());
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

        contentLeft.add(toolbar, grid);

        //Content Right
        contentRight = new Div();
        contentRight.addClassName("content-right");

        add(contentLeft);
        add(contentRight);

//        System.out.println("Reloadedddd");
    }

    private void openFormDialog(Customer customer) {
        CustomerForm formCustomer = new CustomerForm();
        formCustomer.setEntity(customer);
        formCustomer.setSaveEventListener(e -> {
            this.saveCustomer(e);
            formCustomer.close();
        });
        formCustomer.open();
    }

    private void configureGrid() {
        grid.addClassName("customer-grid");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addComponentColumn(customer -> {
            return new H5(customer.getName());
        }).setAutoWidth(true).setHeader("Nombre")
                .setSortable(true)
                .setComparator(Customer::getName)
                .getStyle().set("min-width", "200px");
        /*grid.addColumn(Customer::getName).setAutoWidth(true).setHeader("Nombre")
                .setSortable(true)
                .getStyle().set("min-width", "200px");
         */
        grid.addColumn(Customer::getPhone).setHeader("Teléfono").setSortable(true);
        grid.addColumn(Customer::getEmail).setHeader("Email").setSortable(true);
        grid.addComponentColumn(customer -> {
            SvgIcon star = LineAwesomeIcon.STAR_SOLID.create();
            star.addClassName("star-icon");
            return customer != null && customer.isFavorite() ? star : null;
        }).setHeader("Favorito").setSortable(true).setComparator(Customer::isFavorite).setWidth("6rem");

        createMenu();

        grid.asSingleSelect().addValueChangeListener(e -> showDetail(e.getValue() != null ? e.getValue().getId() : null));
    }

    private void updateList() {
        grid.setItems(customerService.findAll(txtFilter.getValue()));
        count.setText(String.valueOf(customerService.count()));
    }

    public void saveCustomer(Customer customer) {
        customerService.save(customer);
        updateList();
        Notification.show("Cliente Guardado!");
    }

    private void deleteCustomer(Customer customer) {
        ConfirmDialog confirmDialog = new ConfirmDialog("¿Eliminar a \"" + customer.getName() + "\"?",
                "¿Desea borrar el registro?",
                "Eliminar", e -> {
            customerService.delete(customer);
            updateList();
            getUI().ifPresent(ui -> ui.navigate(CustomerView.class));
        }, "Cancelar", e -> e.getSource().close());
        confirmDialog.setConfirmButtonTheme(ButtonVariant.LUMO_ERROR.getVariantName() + " " + ButtonVariant.LUMO_PRIMARY.getVariantName());
        confirmDialog.setCloseOnEsc(true);
        confirmDialog.open();

    }

    private void createMenu() {
        GridContextMenu<Customer> menu = grid.addContextMenu();
        menu.addItem(new HorizontalLayout(new Span("Nuevo"), LineAwesomeIcon.USER_PLUS_SOLID.create()), e -> openFormDialog(new Customer()));
        menu.addItem("Editar", e -> openFormDialog(e.getItem().get()));
        menu.addItem("Detalles", e -> showDetail(e.getItem().get().getId()));
        menu.add(new Hr());
        GridMenuItem itemDel = menu.addItem("Eliminar", e -> deleteCustomer(e.getItem().get()));
        itemDel.addClassNames(LumoUtility.TextColor.ERROR);
    }

    private void showDetail(Long id) {
        if (id != null) {
            getUI().ifPresent(ui -> ui.navigate(CustomerView.class, id));
        } else {
            getUI().ifPresent(ui -> ui.navigate(CustomerView.class));
        }
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long id) {
        if (id != null) {
            if (customerService.findByCustomerId(id) == null) {
//                beforeEvent.rerouteToError(IllegalArgumentException.class, getTranslation("customer.not.found", beforeEvent.getLocation().getPath()));
//                beforeEvent.forwardTo("error");
                NotificationUtil.showContrastCloseable("Registro no encontrado...");
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
                customerviewdetail = new CustomerViewDetail(customerService, id);
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
        }
    }

}
