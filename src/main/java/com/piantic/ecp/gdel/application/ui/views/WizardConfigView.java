package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.entity.Work;
import com.piantic.ecp.gdel.application.backend.service.ProfileService;
import com.piantic.ecp.gdel.application.backend.service.RoleService;
import com.piantic.ecp.gdel.application.backend.service.WorkService;
import com.piantic.ecp.gdel.application.ui.views.forms.ProfileForm;
import com.piantic.ecp.gdel.application.ui.views.forms.WorkForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

@Route("wizard")
public class WizardConfigView extends Div {

    private ProfileService profileService;
    private WorkService workService;
    private TabSheet tabSheet;

    public WizardConfigView(ProfileService profileService, WorkService workService, RoleService roleService) {
        addClassName("wizard-config-view");
        this.profileService = profileService;
        this.workService = workService;

        tabSheet = new TabSheet();
        tabSheet.addClassName("tabsheet-wizard");

        tabSheet.add("PASO 1", getTab1());
        tabSheet.add("PASO 2", getTab2());
        tabSheet.add("PASO 3", getTab3());
        tabSheet.add("PASO 4", getTab1());
        tabSheet.add(new Tab(VaadinIcon.CAR.create(), new Span("Mi Negocio")), getTab1());

        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setWidth("100%");
        btnLayout.addClassNames(LumoUtility.Border.ALL, LumoUtility.BorderColor.CONTRAST_5);
        Button btnNext = new Button("Siguiente", LineAwesomeIcon.ARROW_RIGHT_SOLID.create());
        btnNext.setIconAfterText(true);
        btnNext.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        btnNext.addClickListener(e -> {
            if (tabSheet.getSelectedIndex() < 4) {
                tabSheet.setSelectedIndex(tabSheet.getSelectedIndex() + 1);
            }
        });

        btnLayout.add(btnNext);

        add(tabSheet, btnLayout);
    }

    private Component getTab1() {
        H3 h3 = new H3("Tu Negocio");
        H4 h4 = new H4("Configuremos los datos básicos de tu negocio");

        TextField nameBussiness = new TextField("Nombre de tu negocio");
        Image logo = new Image();

        Div contentTab1 = new Div(h3, h4, nameBussiness, logo);
        contentTab1.addClassNames("content-tab-1");


        return contentTab1;
    }

    private Component getTab2() {
        H3 h3 = new H3("Perfiles");
        Span text1 = new Span(new Span("Los perfiles son las personas que trabajan para ti, que hacen parte de la operación de tu negocio Ej:"),  createBadge("Estilista"), createBadge("Barbero"));
        Html text2 = new Html("<p>Configuremos los <b>Perfiles</b> que operan en tu Negocio</p>");
        Text text3 = new Text("Puedes agregar todos los que desees");

        Div contentTab1 = new Div(h3, text1, text2, text3);
        contentTab1.addClassNames("content-tab-2");

        Grid<Profile> grid = new Grid<>(Profile.class, false);

        Button btnAddService = new Button("Agregar Perfil", LineAwesomeIcon.PLUS_SQUARE.create());
        btnAddService.addThemeVariants(ButtonVariant.LUMO_SMALL);
        btnAddService.addClickListener(e -> {
            ProfileForm profileForm = new ProfileForm();
            profileForm.setEntity(new Profile());
            profileForm.setSaveEventListener(profile -> {
                profileService.save(profile);
                profileForm.close();
                grid.setItems(profileService.findAll());
            });
            profileForm.open();
        });
        contentTab1.add(btnAddService);

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setItems(profileService.findAll());
        grid.addColumn(Profile::getNameProfile).setHeader("Perfiles");
        contentTab1.add(grid);

        return contentTab1;
    }

    private Component getTab3() {
        H3 h3 = new H3("Servicios");
        Span text1 = new Span(new Span("Los servicios es lo que ofreces / vendes / produces en tu negocio Ej:"),  createBadge("Corte Caballero"), createBadge("Manicure"), createBadge("Tinte"));
        Html text2 = new Html("<p>Configuremos los <b>servicios</b> que ofreces en tu Negocio</p>");
        Text text3 = new Text("Puedes agregar todos los que desees");

        H4 h4servicios = new H4("Servicios");

        Grid<Work> grid = new Grid<>(Work.class, false);

        Div contentTab1 = new Div(h3, text1, text2, text3, h4servicios);
        contentTab1.addClassNames("content-tab-3");
        Button btnAddService = new Button("Agregar servicio", LineAwesomeIcon.PLUS_SQUARE.create());
        btnAddService.addThemeVariants(ButtonVariant.LUMO_SMALL);
        btnAddService.addClickListener(e -> {
            WorkForm workForm = new WorkForm(profileService);
            workForm.setEntity(new Work());
            workForm.setSaveEventListener(work_return -> {
                workService.save(work_return);
                workForm.close();
                grid.setItems(workService.findAll());
            });
            workForm.open();
        });

        contentTab1.add(btnAddService);

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setItems(workService.findAll());
        grid.addColumn(Work::getTitle).setHeader("Servicios");
        contentTab1.add(grid);

        return contentTab1;
    }

    private Span createBadge(String text){
        Span badge = new Span(text);
        badge.getElement().getThemeList().add("badge");
        return badge;
    }

}
