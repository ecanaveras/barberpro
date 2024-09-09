package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.Application;
import com.piantic.ecp.gdel.application.backend.entity.Appointment;
import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.service.AppointmentService;
import com.piantic.ecp.gdel.application.backend.service.ProfileService;
import com.piantic.ecp.gdel.application.ui.views.forms.ActivityDetailForm;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@PageTitle("Actividad")
@Route(value = "feed", layout = MainLayout.class)
public class ActivityView extends VerticalLayout implements HasUrlParameter<String> {


    private ProfileService profileService;
    private AppointmentService activities;

    private ActivityDetailForm activityDetailForm = new ActivityDetailForm();

    private Profile currentProfile;

    public ActivityView(AppointmentService activities, ProfileService profileService) {
        addClassName("activity-view");
        this.activities = activities;
        this.profileService = profileService;

        add(createToolbar());

        currentProfile = Application.getProfile();

//        activities.findAppointmentByProfile(Application.getProfile()).forEach(appointment -> {
//            add(createContentLayout(appointment));
//        });

        add(activityDetailForm);

    }

    private void loadInfoActivity(Tab tab, Integer filterDateOption) {
//        if(tab.getLabel().equals()) {}
        //TODO Aplicar logica de negocio de filtrado en el servicio de Actividades
        LocalDate localDate = LocalDate.now();
        switch (filterDateOption) {
            case 2:
                localDate = localDate.minusDays(1);
                break;
            case 3:
                localDate = localDate.minusDays(2);
                break;
            case 4:
                localDate = localDate.minusDays(7);
                break;
            case 6:
                localDate = localDate.minusMonths(1);
                break;
        }
        System.out.println("FECHA: " + localDate);
    }

    private Div createToolbar() {
        Div toolbar = new Div();
        toolbar.addClassName("toolbar-feed");
        //TODO Controlar cuando mostrar estos TABS
        Tabs tabs = new Tabs();
        tabs.addThemeVariants(TabsVariant.LUMO_HIDE_SCROLL_BUTTONS);
        tabs.addThemeVariants(TabsVariant.LUMO_SMALL);
        tabs.addClassNames(LumoUtility.FontSize.SMALL);
        Tab alltab = new Tab("TODOS");
        tabs.add(alltab);
        profileService.findAll().forEach(profile -> {
            tabs.add(new Tab(profile.getNameProfile().toUpperCase()));
        });
        Map<Integer, String> optionFilterDate = new HashMap<>();
        optionFilterDate.put(1, "Hoy");
        optionFilterDate.put(2, "Ayer");
        optionFilterDate.put(3, "Últ. 3 días");
        optionFilterDate.put(4, "Últ. 7 días");
        optionFilterDate.put(5, "Este mes");
        optionFilterDate.put(6, "Mes anterior");

        //TODO Contemplar la opcion PopoverDropdownField en Vaadin (POPOVER)
        ComboBox<Integer> comboFilter = new ComboBox();
        comboFilter.addClassNames(LumoUtility.FontSize.XSMALL);
        comboFilter.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        comboFilter.setItems(optionFilterDate.keySet());
        comboFilter.setItemLabelGenerator(option -> optionFilterDate.get(option));
        comboFilter.setValue(1);
        comboFilter.addValueChangeListener(event -> loadInfoActivity(tabs.getSelectedTab(), comboFilter.getValue()));

        toolbar.add(tabs);
        toolbar.add(comboFilter);

        return toolbar;
    }


    private VerticalLayout createContentLayout(Appointment appointment) {
        VerticalLayout content = new VerticalLayout();
        content.setId(String.valueOf(appointment.getId()));
        content.addClassName("content-feed");
        content.addSingleClickListener(e -> {
            e.getSource().getUI().ifPresent(ui -> ui.navigate(ActivityView.class, e.getSource().getId().toString()));
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

