package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.Application;
import com.piantic.ecp.gdel.application.backend.entity.Appointment;
import com.piantic.ecp.gdel.application.backend.entity.Customer;
import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.service.AppointmentService;
import com.piantic.ecp.gdel.application.backend.service.CustomerService;
import com.piantic.ecp.gdel.application.backend.utils.NotificationUtil;
import com.piantic.ecp.gdel.application.backend.utils.generics.CloseEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public class WorkFinishView extends Div {

    private Long idSelected;
    private Double totalpay;
    List<WorkingView.WorkAdded> listservices;
    private CloseEventListener closeEventListener;
    private CustomerService customerService;
    private Customer customer;
    private boolean saved;
    private Profile profileworking;

    private AppointmentService appointmentService;

    public WorkFinishView(CustomerService customerService, AppointmentService appointmentService, Long idSelected, Stream<WorkingView.WorkAdded> workAddeds, Double totalpay) {
        addClassName("work-finish-view");
        this.idSelected = idSelected;
        this.customerService = customerService;
        this.appointmentService = appointmentService;
        this.totalpay = totalpay;
        this.listservices = workAddeds.toList();

        SvgIcon iconfinish = LineAwesomeIcon.CHECK_CIRCLE_SOLID.create();
        iconfinish.addClassName(LumoUtility.TextColor.SUCCESS);
        iconfinish.addClassName(LumoUtility.IconSize.LARGE);

        H3 title = new H3("Todo Listo...");

        //Profile
        profileworking = (Profile) VaadinSession.getCurrent().getAttribute(Application.SESSION_PROFILE);
        Span spanprofile = new Span("Perfil");
        H4 h4nameprofile = new H4(profileworking.getNameProfile());

        //Cliente
        findCustomer();
        Span spancliente = new Span("Cliente");
        H4 h4name = new H4(this.customer.getName());


        //Servicios
        Span servicios = new Span("Servicios");

        Span spandetservi = new Span();
        spandetservi.getElement().getThemeList().add("badge success");
        spandetservi.setText(String.valueOf(listservices.size()));

        Details detailsServices = new Details(new HorizontalLayout(servicios, spandetservi), getItemServices());
        detailsServices.setOpened(true);

        Span spantotaltoPay = new Span("Total $:");
        Span spantoPay = new Span(formatNumber(this.totalpay));
        spantoPay.addClassName("span-to-pay");
        spantoPay.addClassName(LumoUtility.TextColor.SUCCESS);

        VerticalLayout layout = new VerticalLayout(
                new Div(iconfinish, title, new Span("Verifique que la información es correcta"))
                , spanprofile
                , h4nameprofile
                , spancliente
                , h4name
                , detailsServices
                , new HorizontalLayout(spantotaltoPay, spantoPay)
        );

        layout.setPadding(false);
        layout.setMargin(false);

        add(layout);

        Button btnSave = new Button("Guardar & Continuar");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.setWidthFull();
        btnSave.addClickListener(e -> {
            //Guardar
            Appointment chamba = new Appointment(LocalDateTime.now(), profileworking, customer, totalpay);
            listservices.forEach(workAdded -> {
                Double total = workAdded.getCant() * workAdded.getServicio().getPrice();
                Double commission = workAdded.getServicio().getCommissions();
                Double valcommission = (commission * total)/100;
                Double valrevenue = total - valcommission;
                chamba.addWork(workAdded.getServicio(), workAdded.getCant(), total, valrevenue, commission, valcommission);
            });
            appointmentService.save(chamba);
            this.saved = true;

            NotificationUtil.showSuccess("Trabajo Guardado");
            this.closeView();
        });


        Button btnCancel = new Button("Cancelar");
        btnCancel.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnCancel.setWidthFull();
        btnCancel.addClickListener(e -> this.closeView());
        Div divbotones = new Div();
        divbotones.addClassName("div-botones");
        divbotones.add(btnSave, btnCancel);
        add(divbotones);
    }

    private Div getItemServices() {
        Div divInfoservices = new Div();
        divInfoservices.addClassName("div-infoservices");

        listservices.forEach(workAdded -> {
            Span spanservice = new Span();
            spanservice.getElement().getThemeList().add("badge contrast");
            spanservice.setText(String.format("%1s (%d)", workAdded.getServicio().getTitle(), workAdded.getCant()));
            divInfoservices.add(spanservice);
        });

        return divInfoservices;
    }

    private void findCustomer() {
        this.customer = customerService.findByCustomerId(idSelected);
    }

    private String formatNumber(Double number) {
        return new DecimalFormat("#,###.##").format(number);
    }

    public void closeView() {
        this.setVisible(false);
        if (closeEventListener != null) {
            closeEventListener.onClose();
        }
    }

    public void setCloseEventListener(CloseEventListener closeEventListener) {
        this.closeEventListener = closeEventListener;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}
