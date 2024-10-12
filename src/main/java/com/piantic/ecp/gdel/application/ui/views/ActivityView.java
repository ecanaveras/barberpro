package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.backend.entity.Appointment;
import com.piantic.ecp.gdel.application.backend.service.AppointmentService;
import com.piantic.ecp.gdel.application.backend.service.ProfileService;
import com.piantic.ecp.gdel.application.backend.service.setting.ConfigOptionService;
import com.piantic.ecp.gdel.application.backend.utils.NumberUtil;
import com.piantic.ecp.gdel.application.ui.views.admin.SettingsView;
import com.piantic.ecp.gdel.application.ui.views.components.CardItem;
import com.piantic.ecp.gdel.application.ui.views.details.ActivityViewDetail;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PageTitle("Actividad")
@Route(value = "feed", layout = MainLayout.class)
public class ActivityView extends Div implements HasUrlParameter<Long> {


    private ProfileService profileService;
    private AppointmentService appointmentService;
    private ConfigOptionService configOptionService;

    private ActivityViewDetail activityDetailForm;

    private VerticalLayout content;
    private Tabs tabs;
    private ComboBox<Integer> comboFilter;

    public ActivityView(AppointmentService activities, ProfileService profileService, ConfigOptionService configOptionService) {
        addClassName("activity-view");
        this.appointmentService = activities;
        this.profileService = profileService;
        this.configOptionService = configOptionService;
        activityDetailForm = new ActivityViewDetail(activities, configOptionService);

        add(createToolbar());

        //Info de hoy
        content = new VerticalLayout();
        content.addClassName("activity-content");

        loadInfoActivity(tabs.getSelectedTab(), 1);

        add(activityDetailForm);
        add(content);

    }

    private Component quickActionsNoData() {
        Div divnodata = new Div();
        divnodata.addClassName("quick-actions");
        divnodata.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.AlignSelf.CENTER, LumoUtility.TextAlignment.CENTER
                , LumoUtility.Gap.SMALL, LumoUtility.Padding.XLARGE, LumoUtility.Border.ALL, LumoUtility.BorderRadius.MEDIUM, LumoUtility.BorderColor.CONTRAST_30
        ,LumoUtility.Padding.Top.SMALL, LumoUtility.Padding.Bottom.LARGE);

        Div divactionquick = new Div();
        divactionquick.addClassNames(LumoUtility.Gap.MEDIUM, LumoUtility.Display.FLEX
                , LumoUtility.FlexDirection.COLUMN, LumoUtility.FlexWrap.WRAP, LumoUtility.Gap.SMALL);
        Header header = new Header(new Span("Accesos Directos"));
        header.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.SMALL, LumoUtility.FontWeight.LIGHT);
        divactionquick.add(new CardItem(LineAwesomeIcon.FIRE_ALT_SOLID.create(), "Area de Trabajo", WorkingView.class));
        divactionquick.add(new CardItem(LineAwesomeIcon.CHART_AREA_SOLID.create(), "Dashboard", DashboardView.class));
        divactionquick.add(new CardItem(LineAwesomeIcon.TOILET_SOLID.create(), "Preferencias", SettingsView.class));
        divnodata.add(header, divactionquick);
        return divnodata;
    }


    private void loadInfoActivity(Tab tab, Integer filterDateOption) {
//        if(tab!=null && ) {}
        //TODO Aplicar logica de negocio de filtrado en el servicio de Actividades
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateStart = localDate.atStartOfDay();
        LocalDateTime localDateEnd = localDate.atTime(LocalTime.MAX);
        switch (filterDateOption) {
            case 2: //Ayer
                localDateStart = localDate.minusDays(1).atStartOfDay();
                localDateEnd = localDate.minusDays(1).atTime(LocalTime.MAX);
                break;
            case 3: //Ult 3 dias
                localDateStart = localDate.minusDays(2).atStartOfDay();
                localDateEnd = localDate.atTime(LocalTime.MAX);
                break;
            case 4: //Ult 7 dias
                localDateStart = localDate.minusDays(6).atStartOfDay();
                localDateEnd = localDate.atTime(LocalTime.MAX);
                break;
            case 5: //Mes Actual
                localDateStart = localDate.minusDays(localDate.getDayOfMonth() - 1).atStartOfDay();
                localDateEnd = localDate.plusDays(localDate.lengthOfMonth() - (localDate.getDayOfMonth())).atTime(LocalTime.MAX);
                break;
            case 6: //Mes pasado
                localDate = localDate.minusMonths(1);
                localDateStart = localDate.minusDays(localDate.getDayOfMonth() - 1).atStartOfDay();
                localDateEnd = localDate.plusDays(localDate.lengthOfMonth() - (localDate.getDayOfMonth())).atTime(LocalTime.MAX);
                break;
        }
//        System.out.println("FECHAS: " + localDateStart + " - " + localDateEnd);

        //Load Info
        content.removeAll();
        List<Appointment> result;
        if (tabs.getTabAt(0).equals(tab)) {
            result = appointmentService.findByDateRange(localDateStart, localDateEnd);
            result.forEach(appointment -> {
                content.add(createContentLayout(appointment));
            });
        } else {
            result = appointmentService.findAppointmentByProfileAndDate(Long.valueOf(tab.getId().get()), localDateStart, localDateEnd);
            result.forEach(appointment -> {
                content.add(createContentLayout(appointment));
            });
        }
        tab.getChildren().forEach(children -> {
            if (children.getElement().toString().length() > 20) {
                tab.remove(children);
            }
        });
        tab.add(createBadge(result.size()));

        //Quick Actions
        if (result.isEmpty()) {
            content.removeAll();
            content.addClassNames(LumoUtility.Background.BASE);
            content.setSizeFull();
            Span message = new Span(new Span(LineAwesomeIcon.FILTER_SOLID.create()),new Span("NO HAY ACTIVIDADES"));
            message.getElement().getThemeList().add("badge contrast pill small");
            message.addClassNames(LumoUtility.AlignSelf.CENTER, LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.XLARGE);
            content.add(message);
            content.add(quickActionsNoData());
        }
    }

    private Div createToolbar() {
        Div toolbar = new Div();
        toolbar.addClassName("toolbar-feed");
        toolbar.addClassNames(LumoUtility.Position.STICKY);
        //TODO Controlar cuando mostrar estos TABS
        tabs = new Tabs();
        tabs.addThemeVariants(TabsVariant.LUMO_HIDE_SCROLL_BUTTONS);
        tabs.addThemeVariants(TabsVariant.LUMO_SMALL);
        tabs.addClassNames(LumoUtility.FontSize.SMALL);
        Tab alltab = new Tab(new Span("TODOS"));
        tabs.add(alltab);
        tabs.addSelectedChangeListener(event -> loadInfoActivity(tabs.getSelectedTab(), comboFilter.getValue()));
        profileService.findProfilesActives().forEach(profile -> {
            Tab tab = new Tab(profile.getNameProfile().toUpperCase());
            tab.setId(profile.getId().toString());
            tabs.add(tab);

        });

        Map<Integer, String> optionFilterDate = new HashMap<>();
        optionFilterDate.put(1, "Hoy");
        optionFilterDate.put(2, "Ayer");
        optionFilterDate.put(3, "Últ. 3 días");
        optionFilterDate.put(4, "Últ. 7 días");
        optionFilterDate.put(5, "Este mes");
        optionFilterDate.put(6, "Mes anterior");

        //TODO Contemplar la opcion PopoverDropdownField en Vaadin (POPOVER)
        comboFilter = new ComboBox();
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

    private Span createBadge(int value) {
        Span badge = new Span(String.valueOf(value));
        badge.setId("badge");
        badge.getElement().getThemeList().add("badge small pill");
        badge.getStyle().set("margin-inline-start", "var(--lumo-space-xs)");
        return badge;
    }


    private VerticalLayout createContentLayout(Appointment appointment) {
        VerticalLayout content = new VerticalLayout();
        content.setId(String.valueOf(appointment.getId()));
        content.addClassName("content-feed");
        content.addSingleClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate(ActivityView.class, appointment.getId()));
        });


        Span spanidappoinment = new Span(LineAwesomeIcon.MAGIC_SOLID.create(), new Span(appointment.getId().toString()));
        spanidappoinment.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.TextColor.PRIMARY);
        Div divservices = new Div();
        divservices.addClassName("div-services");
        appointment.getAppointmentWorks().forEach(appointmentWork -> {
            Span service = new Span(appointmentWork.getProduct().getTitle());
            service.getElement().getThemeList().add("badge success");
            service.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.FontWeight.SEMIBOLD);
            divservices.add(service);
        });

        Span spanfecha = new Span(appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        spanfecha.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.LIGHT);

        HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setWidthFull();
        hLayout.addClassNames(
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.Padding.SMALL,
                LumoUtility.Flex.GROW);
        hLayout.add(
                new HorizontalLayout(LineAwesomeIcon.USER_CIRCLE.create(),
                        new Span(appointment.getProfile().getNameProfile())),
                new Span(LineAwesomeIcon.CASH_REGISTER_SOLID.create(), new Span(NumberUtil.formatNumber(appointment.getTotal()))
                ));

        Div divfooter = new Div();
        divfooter.addClassName("div-footer");
        divfooter.add(spanidappoinment, new Span(LineAwesomeIcon.CALENDAR.create(), spanfecha));

        content.add(hLayout, divservices, divfooter);

        content.addClassNames(
                LumoUtility.Padding.MEDIUM,
                LumoUtility.Margin.Bottom.SMALL,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.Background.BASE,
                LumoUtility.Border.ALL);

        return content;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long activityId) {
        if (activityId != null) {
            if (activityDetailForm != null && activityDetailForm.getID() != null && activityDetailForm.getID().equals(activityId) && activityDetailForm.getClassNames().contains("visible")) {
                activityDetailForm.removeClassName("visible");
                activityDetailForm.setID(null);
                return;
            }
            activityDetailForm.addClassName("visible");
            activityDetailForm.setID(activityId);
            activityDetailForm.updateUI();
        }
    }
}

