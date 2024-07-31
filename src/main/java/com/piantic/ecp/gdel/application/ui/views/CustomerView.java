package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.backend.entity.Customer;
import com.piantic.ecp.gdel.application.backend.service.CustomerService;
import com.piantic.ecp.gdel.application.ui.views.forms.CustomerForm;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.context.annotation.Scope;
import org.vaadin.lineawesome.LineAwesomeIcon;

@PageTitle("Clientes | BarberPro")
@Route("customer")
@Scope("prototype")
public class CustomerView extends HorizontalLayout implements HasUrlParameter<Long> {

    private final TextField txtFilter;
    private final Button btnAdd;
    private final Span count = new Span();
    private Div contentRight;
    private VerticalLayout contentLeft;
    private CustomerViewDetail customerviewdetail;
    private Grid<Customer> grid = new Grid<>(Customer.class, false);
    private CustomerService customerService;
    private Boolean detailAdded = false;

    public CustomerView(CustomerService customerService) {
        addClassName("customer-view");
        setSizeFull();


        this.customerService = customerService;

        //Title
        H3 title = new H3("Clientes");
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

        btnAdd = new Button(LineAwesomeIcon.USER_PLUS_SOLID.create());
        btnAdd.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnAdd.addClickListener(click -> {
            openFormDialog(null);
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

    private void openFormDialog(Customer customer) {
        CustomerForm formCustomer = new CustomerForm();
        formCustomer.setCustomer(customer != null ? customer : new Customer());
        formCustomer.addListener(CustomerForm.SaveEvent.class, e -> {
            this.saveCustomer(e);
            formCustomer.close();
        });
        formCustomer.open();
    }

    private void configureGrid() {
        grid.addClassName("customer-grid");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addComponentColumn(customer ->
                new Anchor(String.format("/customer/%d", customer.getId()), customer.getName())
        ).setKey("name1").setAutoWidth(true).setHeader("Nombre").setSortable(true).getStyle().set("min-width", "200px");
        grid.addColumn(Customer::getPhone).setHeader("Teléfono").setSortable(true);
        grid.addColumn(Customer::getEmail).setHeader("Email").setSortable(true);
        grid.addComponentColumn(customer -> {
            SvgIcon star = LineAwesomeIcon.STAR_SOLID.create();
            star.addClassName("star-icon");
            return customer != null && customer.isFavorite() ? star : null;
        }).setHeader("Favorito").setWidth("6rem");

        createMenu();

        grid.asSingleSelect().addValueChangeListener(e -> showDetail(e.getValue() != null ? e.getValue().getId() : null));
    }

    private void updateList() {
        grid.setItems(customerService.findAll(txtFilter.getValue()));
        count.setText(String.valueOf(customerService.count()));
    }

    public void saveCustomer(CustomerForm.SaveEvent saveEvent) {
        customerService.save(saveEvent.getCustomer());
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
        menu.addItem(new HorizontalLayout(new Span("Nuevo"), LineAwesomeIcon.USER_PLUS_SOLID.create()), e -> openFormDialog(null));
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
