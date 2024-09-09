package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.entity.Tenand;
import com.piantic.ecp.gdel.application.backend.entity.Work;
import com.piantic.ecp.gdel.application.backend.service.ProfileService;
import com.piantic.ecp.gdel.application.backend.service.TenandService;
import com.piantic.ecp.gdel.application.backend.service.WorkService;
import com.piantic.ecp.gdel.application.backend.utils.NotificationUtil;
import com.piantic.ecp.gdel.application.ui.views.forms.ProfileForm;
import com.piantic.ecp.gdel.application.ui.views.forms.WorkForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Route("wizard")
public class WizardConfigView extends Div {

    private ProfileService profileService;
    private TenandService tenandService;
    private WorkService workService;
    private TabSheet tabSheet;

    public WizardConfigView(ProfileService profileService, WorkService workService, TenandService tenandService) {
        addClassName("wizard-config-view");
        this.profileService = profileService;
        this.workService = workService;
        this.tenandService = tenandService;

        tabSheet = new TabSheet();
        tabSheet.addClassName("tabsheet-wizard");

        tabSheet.add("PASO 1", getTab1());
        tabSheet.add("PASO 2", getTab2());
        tabSheet.add("PASO 3", getTab3());
        tabSheet.add("PASO 4", getTab4());
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
                btnLayout.setVisible(true);
            }
        });

        tabSheet.addSelectedChangeListener(l -> {
            if (tabSheet.getSelectedIndex() == 3) {
                btnLayout.setVisible(false);
            }
        });

        btnLayout.add(btnNext);

        add(tabSheet, btnLayout);
    }

    private Component getTab1() {
        H3 h3 = new H3("Tu Negocio");
        H4 h4 = new H4("Configuremos los datos básicos de tu negocio");

        H1 nameTenand = new H1();

        TextField nameBussiness = new TextField("Nombre de tu negocio");
        // Listener para actualizar la vista previa del nombre
        nameBussiness.addValueChangeListener(event -> {
            String companyName = event.getValue();
            nameTenand.setText(companyName);
        });

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setDropLabel(new Span("Arrastre aquí su Logo"));

        NativeLabel dropEnabledLabel = new NativeLabel("Suba o arrastre aquí su Logo");
        dropEnabledLabel.getStyle().set("font-weight", "600");
        upload.setId("upload-drop-enabled");
        upload.setAcceptedFileTypes(".png", ".jpg", ".gif", ".webp");
        upload.setMaxFiles(1);
        Button btnUpload = new Button("Subir Logo");
        btnUpload.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        upload.setUploadButton(btnUpload);
        upload.getElement().addEventListener("max-files-reached-changed", event -> {
            boolean maxFilesReached = event.getEventData().getBoolean("event.detail.value");
            btnUpload.setEnabled(!maxFilesReached);
        }).addEventData("event.detail.value");

        int maxFileSizeInBytes = 10 * 1024 * 1024; // 10MB
        upload.setMaxFileSize(maxFileSizeInBytes);
        dropEnabledLabel.setFor(upload.getId().get());
        upload.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();
            NotificationUtil.showError(errorMessage);
        });


        Image logoTenand = new Image();
        Button btnSaveEmpresa = new Button("Guardar");

        // Configuración del listener del Upload
        upload.addSucceededListener(event -> {
            // Obtiene los bytes del archivo cargado
            try {
                byte[] logoBytes = null;
                logoBytes = buffer.getInputStream().readAllBytes();
                // Crea un StreamResource para mostrar la imagen
                byte[] finalLogoBytes = logoBytes;
                StreamResource resource = new StreamResource(event.getFileName(), () -> new ByteArrayInputStream(finalLogoBytes));
                logoTenand.setSrc(resource);
                logoTenand.setVisible(true); // Muestra la vista previa del logo
                btnSaveEmpresa.setEnabled(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        Div contentTab1 = new Div(h3, h4);
        contentTab1.addClassNames("content-tab-1");

        Binder<Tenand> binder = new Binder();
        binder.setBean(new Tenand());

//        binder.forField(nameBussiness).bind(Tenand::getNameTenand);
        FormLayout formLayout = new FormLayout();
        formLayout.add(nameBussiness, upload);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        contentTab1.add(formLayout);

        Div divpreview = new Div();
        divpreview.addClassName("div-preview");

        H5 h5 = new H5("Vista Previa");
        h5.addClassName(LumoUtility.TextColor.TERTIARY);

        btnSaveEmpresa.setEnabled(false);
        btnSaveEmpresa.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSaveEmpresa.addClickListener(e -> {
            Tenand tenand = new Tenand();
            tenand.setNameTenand(nameTenand.getText());
            tenand.setEmail("baberpro");
            try {
                tenand.setLogo(buffer.getInputStream().readAllBytes());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            tenandService.save(tenand);
            NotificationUtil.showSuccess("Datos del Negocio Actualizados");
        });

        //Load info if exist
        //TODO Configurar busqueda de Tenand de acuerdo al correo Logueado
        tenandService.findAll().forEach(tenand -> {
            nameBussiness.setValue(tenand.getNameTenand());
            StreamResource resource = new StreamResource("logo.png", () -> new ByteArrayInputStream(tenand.getLogo()));
            logoTenand.setSrc(resource);
            btnSaveEmpresa.setEnabled(true);
        });

        divpreview.add(h5, nameTenand, logoTenand, btnSaveEmpresa);

        contentTab1.add(divpreview);
        return contentTab1;
    }

    private Component getTab2() {
        H3 h3 = new H3("Perfiles");
        Span text1 = new Span(new Span("Los perfiles son las personas que trabajan para ti, que hacen parte de la operación de tu negocio Ej:"), createBadge("Estilista"), createBadge("Barbero"));
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
        H3 h3 = new H3("Servicios | Productos");
        Span text1 = new Span(new Span("Los servicios ó productos es lo que ofreces / vendes / produces / consumes en tu negocio Ej:"), createBadge("Corte Caballero"), createBadge("Manicure"), createBadge("Tinte"));
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

    private Component getTab4() {
        Div divpreview = new Div();
        divpreview.addClassName("div-preview");

        H5 h5 = new H5("Felicidades");
        h5.addClassName(LumoUtility.TextColor.TERTIARY);

        H1 nameTenand = new H1();
        Image logoTenand = new Image();

        tenandService.findAll().forEach(tenand -> {
            nameTenand.setText(tenand.getNameTenand());
            StreamResource resource = new StreamResource("logo.png", () -> new ByteArrayInputStream(tenand.getLogo()));
            logoTenand.setSrc(resource);
        });

        Button btnFinish = new Button("PLAY");
        btnFinish.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(WelcomeView.class)));

        divpreview.add(h5, nameTenand, logoTenand, btnFinish);
        return divpreview;
    }

    private Span createBadge(String text) {
        Span badge = new Span(text);
        badge.getElement().getThemeList().add("badge");
        return badge;
    }

}
