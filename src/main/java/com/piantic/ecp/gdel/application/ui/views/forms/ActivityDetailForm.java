package com.piantic.ecp.gdel.application.ui.views.forms;

import com.piantic.ecp.gdel.application.backend.entity.Appointment;
import com.piantic.ecp.gdel.application.backend.service.AppointmentService;
import com.piantic.ecp.gdel.application.backend.service.setting.ConfigOptionService;
import com.piantic.ecp.gdel.application.backend.utils.MessagesUtil;
import com.piantic.ecp.gdel.application.backend.utils.NotificationUtil;
import com.piantic.ecp.gdel.application.backend.utils.NumberUtil;
import com.piantic.ecp.gdel.application.ui.views.ActivityView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;

public class ActivityDetailForm extends Div {

    FormLayout formLayout = new FormLayout();
    Long ID;


    AppointmentService appointmentService;

    private ConfigOptionService configOptionService;

    private Appointment appointment;


    public ActivityDetailForm(AppointmentService appointmentService, ConfigOptionService configOptionService) {
        this.appointmentService = appointmentService;
        this.configOptionService = configOptionService;
        configureView(null);
    }

    public ActivityDetailForm(AppointmentService appointmentService, ConfigOptionService configOptionService, Long id) {
        this.appointmentService = appointmentService;
        this.configOptionService = configOptionService;
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
        tabSheet.add("Opciones", getOpciones());
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
        layout.add(cardItem(LineAwesomeIcon.WALLET_SOLID.create(), "Negocio $", NumberUtil.formatNumber(sumGan.get())));
        layout.add(cardItem(LineAwesomeIcon.HAND_HOLDING_SOLID.create(), "Perfil $", NumberUtil.formatNumber(sumPer.get())));
        if (appointment.getAppointmentWorks().size() == 1)
            layout.add(cardItem(LineAwesomeIcon.PERCENTAGE_SOLID.create(), "Porcentaje", NumberUtil.formatNumber(porc.get())));

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


    private Component getOpciones() {
        VerticalLayout layout = new VerticalLayout();
        H4 title = new H4("Opciones");
        title.addClassNames(LumoUtility.FontWeight.SEMIBOLD);
        Button btnDelete = new Button("Eliminar Actividad", LineAwesomeIcon.TRASH_ALT.create());
        btnDelete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        btnDelete.addClassNames(LumoUtility.Margin.AUTO);

        btnDelete.setEnabled(false);

        AtomicReference<Div> messageInfo =  new AtomicReference<>(MessagesUtil.showWarning("IMPORTANTE: Las actividades eliminadas afectan las cifras y demás datos relacionados, actúe con precaución!"));

        //Eliminar Actividad
        configOptionService.findByName(ConfigOptionService.ENABLE_OPTION_ACTIVIDAD_DELETE_WORK).ifPresent(o -> {
            if(o.getConfigvalue().equals("true")) {
                btnDelete.setEnabled(true);
            }else{
                messageInfo.set(MessagesUtil.showTertiary("AVISO: La configuración de borrar actividad no se encuentra habilitada"));
            }
        });



        btnDelete.addClickListener(e -> {
            Span idactividad = new Span(appointment.getId().toString());
            idactividad.getElement().getThemeList().add("badge");
            ConfirmDialog dialog = getConfirmDialog(idactividad);
            dialog.addConfirmListener(l -> {
                appointment.setEnabled(false);
                appointmentService.save(appointment);
                NotificationUtil.showSuccess("Actividad eliminada");
                dialog.close();
                getUI().ifPresent(ui -> ui.navigate(ActivityView.class));
                UI.getCurrent().refreshCurrentRoute(true);
                this.removeClassName("visible");
            });
            dialog.open();
        });

        btnDelete.addClickListener(e -> {
            Span idactividad = new Span(appointment.getId().toString());
            idactividad.getElement().getThemeList().add("badge");

            ConfirmDialog dialog = getConfirmDialog(idactividad);
            dialog.addConfirmListener(l -> {
                appointment.setEnabled(false);
                appointmentService.save(appointment);
                NotificationUtil.showSuccess("Actividad eliminada");
                dialog.close();
                getUI().ifPresent(ui -> ui.navigate(ActivityView.class));
                UI.getCurrent().refreshCurrentRoute(true);
                this.removeClassName("visible");
            });

            dialog.open();
        });

        //Bloquear Actividad (sí está conigurado)
        if(appointment!= null && Duration.between(appointment.getAppointmentTime(), LocalDateTime.now()).toHours() > 1) {
            configOptionService.findByName(ConfigOptionService.ENABLE_OPTION_ACTIVIDAD_BLOQUEO).ifPresent(o -> {
                if(o.getConfigvalue().equals("true")) {
                    btnDelete.setEnabled(false);
                    messageInfo.set(MessagesUtil.showTertiary("AVISO: La actividad se ha bloquedo debido a la configuración, no es posible borrarla."));
                };
            });
        }

        layout.add(title, messageInfo.get(), btnDelete);

        return layout;
    }

    private static ConfirmDialog getConfirmDialog(Span idactividad) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Eliminar Actividad");
        dialog.setText(new Span(new Span("¿Seguro que desea eliminar la Actividad? "), idactividad));
        dialog.setCloseOnEsc(true);
        dialog.setCancelable(true);
        dialog.setCancelText("Cancelar");
        dialog.setConfirmText("Eliminar");
        dialog.setConfirmButtonTheme(ButtonVariant.LUMO_ERROR.getVariantName() + " " + ButtonVariant.LUMO_PRIMARY.getVariantName());
        dialog.setCancelButtonTheme(ButtonVariant.LUMO_TERTIARY.getVariantName());
        dialog.addCancelListener(e -> e.getSource().close());
        return dialog;
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
