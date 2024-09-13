package com.piantic.ecp.gdel.application.ui.views.forms;

import com.piantic.ecp.gdel.application.backend.entity.Appointment;
import com.piantic.ecp.gdel.application.backend.service.AppointmentService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;

public class ActivityDetailForm extends Div {

    FormLayout formLayout = new FormLayout();
    Long ID;
    AppointmentService appointmentService;
    private Appointment appointment;

    public ActivityDetailForm(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
        configureView(null);
    }

    public ActivityDetailForm(AppointmentService appointmentService, Long id) {
        this.appointmentService = appointmentService;
        configureView(id);
    }

    private void configureView(Long id) {
        addClassName("slide-in-div");
        this.ID = id;

        appointment = appointmentService.findByAppointmentId(ID);

        configureForm();
        configureView();
    }

    private void configureView() {
        addClassNames(LumoUtility.BoxShadow.MEDIUM);

        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.addClassNames(LumoUtility.Display.FLEX,
                LumoUtility.AlignContent.BETWEEN,
                LumoUtility.AlignItems.CENTER);
        Span spanid = new Span(this.ID != null ? this.getID().toString() : "");
        spanid.getElement().getThemeList().add("badge");
        spanid.addClassNames(LumoUtility.Margin.Left.SMALL);
        Span title = new Span(new H3("Detalles "), spanid);
        title.addClassNames(LumoUtility.Display.FLEX);
//        Span state = new Span("Por aprobar");
//        state.getElement().getThemeList().add("bagde contrast");
        Button btnClose = new Button(LineAwesomeIcon.TIMES_CIRCLE.create(), buttonClickEvent -> this.removeClassName("visible"));
        btnClose.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);
        btnClose.addClassName(LumoUtility.Margin.Left.AUTO);
        btnClose.addClickShortcut(Key.ESCAPE);
//        header.add(title, state, btnClose);
        header.add(title, btnClose);

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.add(header, formLayout);
        add(layout);
    }


    private void configureForm() {
        TabSheet tabSheet = new TabSheet();
        tabSheet.setWidthFull();
        tabSheet.add("Actividad", getAppointment());
        tabSheet.add("Rentabilidad", getRentabilidad());
        formLayout.add(tabSheet);

    }

    private Component getRentabilidad() {
        if (appointment == null) {
            return new Span("No data");
        }
        VerticalLayout layout = new VerticalLayout();
        H4 title = new H4("Info $$");
        title.addClassNames(LumoUtility.FontWeight.SEMIBOLD);
        layout.add(title);
        layout.add(cardItem(LineAwesomeIcon.USER.create(), "Perfil", appointment.getProfile().getNameProfile()));
        layout.add(cardItem(LineAwesomeIcon.CASH_REGISTER_SOLID.create(), "Cobrado", appointment.getTotal().toString()));
        AtomicReference<Double> sumGan = new AtomicReference<>(0.0);
        AtomicReference<Double> sumPer = new AtomicReference<>(0.0);
        AtomicReference<Double> porc = new AtomicReference<>(0.0);
        appointment.getAppointmentWorks().forEach(appointmentWork -> {
            sumGan.set(sumGan.get() + appointmentWork.getRevenue());
            sumPer.set(sumPer.get() + appointmentWork.getValueCommision());
            porc.set(appointmentWork.getCommissions());
        });
        layout.add(cardItem(LineAwesomeIcon.WALLET_SOLID.create(), "Negocio $", new DecimalFormat("#,###.##").format(sumGan.get())));
        layout.add(cardItem(LineAwesomeIcon.HAND_HOLDING_SOLID.create(), "Perfil $", new DecimalFormat("#,###.##").format(sumPer.get())));
        if (appointment.getAppointmentWorks().size() == 1)
            layout.add(cardItem(LineAwesomeIcon.PERCENTAGE_SOLID.create(), "Porcentaje", new DecimalFormat("###.##").format(porc.get())));

        return layout;
    }

    private Component getAppointment() {
        if (appointment == null) {
            return new Span("No data");
        }
        VerticalLayout layout = new VerticalLayout();
        H4 cliente = new H4("Info del Cliente");
        cliente.addClassNames(LumoUtility.FontWeight.SEMIBOLD);
        layout.add(cliente);
        layout.add(cardItem(LineAwesomeIcon.OBJECT_GROUP.create(), "Cliente", appointment.getCustomer().getName()));
        if (appointment.getCustomer().isFavorite())
            layout.add(cardItem(LineAwesomeIcon.STAR.create(), "Favorito", "Favorito"));
        H4 title = new H4("Info de la Actividad");
        title.addClassNames(LumoUtility.FontWeight.SEMIBOLD);
        layout.add(title);
        layout.add(cardItem(LineAwesomeIcon.USER.create(), "Perfil", appointment.getProfile().getNameProfile()));
        layout.add(cardItem(LineAwesomeIcon.CALENDAR.create(), "Fecha", appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        layout.add(cardItem(LineAwesomeIcon.MONEY_BILL_WAVE_SOLID.create(), "Valor", appointment.getTotal().toString()));
        layout.add(cardItem(LineAwesomeIcon.MAGIC_SOLID.create(), "Cant. Servicios/Productos", appointment.getAppointmentWorks().size() + ""));
        return layout;
    }


    public void updateUI() {
        removeAll();
        formLayout = new FormLayout();
        this.configureView(this.getID());
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

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }
}
