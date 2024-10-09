package com.piantic.ecp.gdel.application.ui.views.details;

import com.piantic.ecp.gdel.application.backend.entity.Appointment;
import com.piantic.ecp.gdel.application.backend.entity.Customer;
import com.piantic.ecp.gdel.application.backend.service.CustomerService;
import com.piantic.ecp.gdel.application.backend.utils.NumberUtil;
import com.piantic.ecp.gdel.application.ui.views.CustomerView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.time.format.DateTimeFormatter;


public class CustomerViewDetail extends VerticalLayout {


    private final CustomerService customerService;
    private final Long id;
    private Customer customer;
    private Div content;
    Binder<Customer> binder = new Binder<>(Customer.class);

    public CustomerViewDetail(CustomerService customerService, Long id) {
        this.customerService = customerService;
        this.id = id;
        addClassNames("customer-detail-view");
//        setWidthFull();
        configInitUI();
        updateUI(id);
    }

    private void configInitUI() {
        //Header
        Header header = new Header();
        header.addClassName("view-header");
        header.addClassName(LumoUtility.Margin.Bottom.SMALL);
        header.setWidthFull();
        H3 title = new H3("Cliente - Detalles");
        title.addClassNames(LumoUtility.FontWeight.SEMIBOLD);
        Button btnBack = new Button(LineAwesomeIcon.ARROW_LEFT_SOLID.create(), e -> back());
        btnBack.addClassNames("back-button");
        btnBack.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);
//        btnBack.addClassNames(LumoUtility.Margin.Left.AUTO);
        header.add(btnBack);
        header.add(title);

        add(header);
    }

    public void updateUI(Long id) {
//        System.out.printf("Customer: %d\n", id);
        if (id == null) {
            return;
        }
        getData(id);
        H4 personalInfo = new H4("Información Personal");
        H4 contactInfo = new H4("Información de Contacto");
        H4 activityInfo = new H4("Actividad Reciente");
        H4 activityCountInfo = new H4("Total de Citas");

        if (content != null) {
            remove(content);
            content = null;
        }

        if (content == null)
            content = new Div();
        content.setWidthFull();

        content.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Gap.MEDIUM);

        content.add(personalInfo);

        content.add(cardItem(LineAwesomeIcon.USER_CIRCLE.create(), "Nombre", customer.getName()));

        content.add(contactInfo);

        content.add(cardItem(LineAwesomeIcon.PHONE_SOLID.create(), "Telefono", customer.getPhone()));
        content.add(cardItem(LineAwesomeIcon.AT_SOLID.create(), "Email", customer.getEmail()));

        content.add(activityCountInfo);

        content.add(cardItem(LineAwesomeIcon.CALENDAR_SOLID.create(), "Visitas | Citas", NumberUtil.formatNumber(customerService.countByCustomerId(customer))));

        content.add(activityInfo);

        Grid<Appointment> gridlastappoint = new Grid<>(Appointment.class, false);
        gridlastappoint.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COMPACT);
        gridlastappoint.setItems(customerService.getTenLastAppoimentsByCustomerId(customer));
        gridlastappoint.addComponentColumn(appointment-> new Span(appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm"))))
                .setAutoWidth(true).setFlexGrow(1);
        gridlastappoint.addComponentColumn(appointment -> new Span(appointment.getCustomer().getName())).setAutoWidth(true);
        gridlastappoint.addComponentColumn(appointment -> new Span(NumberUtil.formatNumber(appointment.getTotal()))).setAutoWidth(true);

        content.add(gridlastappoint);




        add(content);
    }


    public Div cardItem(SvgIcon iconInfo, String label_, String data) {
        Div divItem = new Div();
        divItem.addClassName("div-content-item");
        divItem.addClassName(LumoUtility.Gap.MEDIUM);
        Div divIcon = new Div();
        divIcon.addClassName("icon-item");

        divIcon.add(iconInfo);

        H5 label = new H5(label_);
        label.addClassNames(LumoUtility.Margin.NONE);

        Span info = new Span(data);

        divItem.add(divIcon, new Div(label, info));
        return divItem;
    }

    private void getData(Long id) {
        this.customer = customerService.findByCustomerId(id);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void back() {
        getUI().ifPresent(ui -> ui.navigate(CustomerView.class));
    }

}
