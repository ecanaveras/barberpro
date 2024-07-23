package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.backend.entity.Customer;
import com.piantic.ecp.gdel.application.backend.service.CustomerService;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class CustomerViewDetail extends VerticalLayout {


    private final CustomerService customerService;
    private final Long id;
    private Customer customer;

    public CustomerViewDetail() {
        this.customerService = null;
        this.id = null;
        addClassNames("customer-detail-view");
    }

    public CustomerViewDetail(CustomerService customerService, Long id) {
        this.customerService = customerService;
        this.id = id;
        addClassNames("customer-detail-view", "visible");

        setWidthFull();

        H3 title = new H3("Cliente - Detalles");
        title.addClassNames(LumoUtility.FontWeight.SEMIBOLD);

        H4 personalInfo = new H4("Información Personal");
        H4 contactInfo = new H4("Información de Contacto");
        H4 activityInfo = new H4("Actividad Reciente");

        getData();

        add(title);

        add(personalInfo);

        add(cardItem(LineAwesomeIcon.USER_CIRCLE.create(), "Nombre", customer.getName()));

        add(contactInfo);

        add(cardItem(LineAwesomeIcon.PHONE_SOLID.create(), "Telefono", customer.getPhone()));

        add(activityInfo);

        //TODO Actividad
        //add(cardItem(LineAwesomeIcon.PHONE_SOLID.create(), "Telefono", customer.getPhone()));
    }


    public Div cardItem(SvgIcon iconInfo, String label_, String data) {
        Div divItem = new Div();
        divItem.addClassName("div-content-item");
        Div divIcon = new Div();
        divIcon.addClassName("icon-item");

        divIcon.add(iconInfo);

        H5 label = new H5(label_);
        label.addClassNames(LumoUtility.Margin.NONE, LumoUtility.FontSize.SMALL);

        Span info = new Span(data);

        divItem.add(divIcon, new Div(label, info));
        return divItem;
    }

    private void getData() {
        customer = customerService.findByCustomerId(id);
    }
}
