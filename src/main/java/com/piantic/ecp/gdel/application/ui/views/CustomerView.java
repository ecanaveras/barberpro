package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.backend.entity.Customer;
import com.piantic.ecp.gdel.application.backend.service.CustomerService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

@Route("customer")
public class CustomerView extends VerticalLayout implements HasUrlParameter<Long> {

    private final TextField txtFilter;
    private final Button btnAdd;
    private final Span count = new Span();
    private final Div content;
    private CustomerViewDetail customerviewdetail;
    private Grid<Customer> grid = new Grid<>(Customer.class);
    private CustomerService customerService;

    public CustomerView(CustomerService customerService) {
        addClassName("customer-view");
        setSizeFull();

        content = new Div();
        content.setSizeFull();
        content.addClassName("content-div");
        this.customerService = customerService;

        Span title = new Span("Clientes");
        title.addClassNames(
                LumoUtility.FontWeight.BOLD,
                LumoUtility.FontSize.MEDIUM);
        count.addClassNames(LumoUtility.FontWeight.LIGHT);
        txtFilter = new TextField();
        txtFilter.setClearButtonVisible(true);
        txtFilter.setPlaceholder("Filtrar...");
        txtFilter.setValueChangeMode(ValueChangeMode.LAZY);
        txtFilter.addValueChangeListener(e -> updateList());

        btnAdd = new Button(LineAwesomeIcon.PLUS_SOLID.create());
        //btnAdd.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

        HorizontalLayout toolbar = new HorizontalLayout(txtFilter, btnAdd);

        configureGrid();

        updateList();
        //customerviewdetail=new CustomerViewDetail();
        //content.add(customerviewdetail);
        add(new HorizontalLayout(title, count), toolbar, grid, content);
    }

    private void configureGrid() {
        grid.addClassName("grid-customer");
        grid.setSizeFull();
        grid.setColumns("name", "phone");
        grid.getColumnByKey("name").setEditorComponent(new TextField());
        grid.addComponentColumn(customer -> {
            return customer.isFavorite() ? LineAwesomeIcon.STAR_SOLID.create() : null;
        }).setHeader("Favorito");

        grid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                getUI().ifPresent(ui -> ui.navigate(CustomerView.class, e.getValue().getId()));
            } else {
                customerviewdetail.removeClassName("visible");
                getUI().ifPresent(ui -> ui.navigate(CustomerView.class));
            }
        });
    }

    private void updateList() {
        grid.setItems(customerService.findAll(txtFilter.getValue()));
        count.setText(String.valueOf(customerService.count()));
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long id) {
        if (id != null) {
            content.removeAll();
            customerviewdetail = new CustomerViewDetail(customerService, id);
            customerviewdetail.addClassNames("visible");
            content.add(customerviewdetail);
        } else {
            if (customerviewdetail != null) {
                customerviewdetail.removeClassName("visible");
            }
        }
    }
}
