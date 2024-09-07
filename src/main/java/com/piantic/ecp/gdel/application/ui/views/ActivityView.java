package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.Application;
import com.piantic.ecp.gdel.application.backend.entity.Appointment;
import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.service.AppointmentService;
import com.piantic.ecp.gdel.application.ui.views.forms.ActivityDetailForm;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

@PageTitle("Actividad")
@Route(value = "feed", layout = MainLayout.class)
public class ActivityView extends VerticalLayout implements HasUrlParameter<String> {


    private AppointmentService activities;

    private ActivityDetailForm activityDetailForm = new ActivityDetailForm();

    public ActivityView(AppointmentService activities) {
        addClassName("activity-view");
        this.activities = activities;

        activities.findAppointmentByProfile((Profile) VaadinSession.getCurrent().getAttribute(Application.SESSION_PROFILE)).forEach(appointment -> {
            add(createContentLayout(appointment));
        });

        add(activityDetailForm);
    }

    private void addEvents(VerticalLayout form) {
        form.getUI().ifPresent(ui -> ui.navigate(ActivityView.class, form.getId().toString()));
    }

    private VerticalLayout createContentLayout(Appointment appointment) {
        VerticalLayout content = new VerticalLayout();
        content.setId(String.valueOf(appointment.getId()));
        content.addClassName("content-feed");
        content.addSingleClickListener(e -> {
            addEvents(e.getSource());
        });

        Span span = new Span(String.valueOf(appointment.getId()));
        span.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.TextColor.PRIMARY);
        Div divservices = new Div();
        divservices.addClassName("div-services");
        appointment.getAppointmentWorks().forEach(appointmentWork -> {
            Span service = new Span(appointmentWork.getWork().getTitle());
            service.getElement().getThemeList().add("badge success");
            service.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.FontWeight.SEMIBOLD);
            divservices.add(service);
        });

        Span span2 = new Span(appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        span2.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.LIGHT);

        HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setWidthFull();
        hLayout.addClassNames(
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.BorderColor.CONTRAST_20,
                LumoUtility.Background.CONTRAST_10,
                LumoUtility.Padding.SMALL,
                LumoUtility.Flex.GROW);
        hLayout.add(
                new HorizontalLayout(LineAwesomeIcon.USER_CIRCLE.create(),
                new Span(appointment.getProfile().getNameProfile())),
                new Span(new DecimalFormat("#.##").format(appointment.getTotal())),
                LineAwesomeIcon.PLUS_SOLID.create());

        content.add(span, divservices, span2, hLayout);

        content.addClassNames(
                LumoUtility.Padding.MEDIUM,
                LumoUtility.Margin.Bottom.SMALL,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.Background.BASE,
                LumoUtility.BorderColor.CONTRAST_5,
                LumoUtility.Border.ALL);

        return content;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String s) {
        if (s != null) {
            if (activityDetailForm != null && activityDetailForm.getID() != null && activityDetailForm.getID().equals(s) && activityDetailForm.getClassNames().contains("visible")) {
                activityDetailForm.removeClassName("visible");
                activityDetailForm.setID(null);
                return;
            }
            activityDetailForm.addClassName("visible");
            activityDetailForm.setID(s);
            activityDetailForm.updateUI();
        }
    }
}

