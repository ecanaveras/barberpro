package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.Application;
import com.piantic.ecp.gdel.application.backend.entity.Product;
import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.entity.Tenant;
import com.piantic.ecp.gdel.application.backend.service.ProfileService;
import com.piantic.ecp.gdel.application.backend.service.TenandService;
import com.piantic.ecp.gdel.application.backend.service.ProductService;
import com.piantic.ecp.gdel.application.backend.utils.NotificationUtil;
import com.piantic.ecp.gdel.application.ui.views.forms.ProfileForm;
import com.piantic.ecp.gdel.application.ui.views.forms.ProductForm;
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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Route("wizard")
public class WizardConfigView extends Div {

    private ProfileService profileService;
    private TenandService tenandService;
    private ProductService productService;
    private TabSheet tabSheet;
    private Tenant tenant;
    private MemoryBuffer buffer;

    private Binder<Tenant> binder = new Binder<>();
    private Image logoTenandTab4;

    public WizardConfigView(ProfileService profileService, ProductService productService, TenandService tenandService) {
        addClassName("wizard-config-view");
        this.profileService = profileService;
        this.productService = productService;
        this.tenandService = tenandService;

        //Load info if exist
        tenant = tenandService.findTenantByEmail(Application.getEmailAccount());
        VaadinSession.getCurrent().setAttribute(Application.SESSION_TENANT, tenant);


        tabSheet = new TabSheet();
        tabSheet.addClassName("tabsheet-wizard");
        tabSheet.addThemeVariants(TabSheetVariant.LUMO_TABS_EQUAL_WIDTH_TABS);

        Tab tab1 = new Tab(LineAwesomeIcon.ADDRESS_CARD_SOLID.create(), new Span("Negocio"));
        Tab tab2 = new Tab(LineAwesomeIcon.USER_SOLID.create(), new Span("Perfiles"));
        Tab tab3 = new Tab(LineAwesomeIcon.MAGIC_SOLID.create(), new Span("Servicios"));
        Tab tab4 = new Tab(VaadinIcon.CHECK_CIRCLE_O.create(), new Span("Finalizar"));


        tabSheet.add(tab1, getTab1());
        tabSheet.add(tab2, getTab2());
        tabSheet.add(tab3, getTab3());
        tabSheet.add(tab4, getTab4());

        for (Tab tab : new Tab[]{tab1, tab2, tab3, tab4}) {
            tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        }


        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setWidth("100%");
        btnLayout.addClassNames(LumoUtility.Border.ALL, LumoUtility.BorderColor.CONTRAST_5);
        Button btnNext = new Button("Siguiente", LineAwesomeIcon.ARROW_RIGHT_SOLID.create());
        btnNext.setIconAfterText(true);
        btnNext.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        btnNext.addClickListener(e -> {
            if (tabSheet.getSelectedTab() == tab1) {
                if (binder.getBean() != null && binder.getBean().getId() == null) {
                    saveTenandInfo(binder.getBean());
                    tenant = tenandService.findTenantByEmail(Application.getEmailAccount());
                }
            }
            if (tabSheet.getSelectedIndex() < 4) {
                tabSheet.setSelectedIndex(tabSheet.getSelectedIndex() + 1);
            }
        });

        tabSheet.addSelectedChangeListener(l -> {
            if (tabSheet.getSelectedIndex() == 3) {
                btnLayout.setVisible(false);
                tabSheet.add(tab4, getTab4());
            } else {
                btnLayout.setVisible(true);
            }
        });

        btnLayout.add(btnNext);

        add(tabSheet, btnLayout);
    }

    private Component getTab1() {
        H3 h3 = new H3("Tu Negocio");
        Span text1 = new Span(new Span("Configuremos los datos básicos de tu negocio"), createBadge("Nombre"), createBadge("Logo"));

        H1 nameTenand = new H1();

        TextField nameBussiness = new TextField("Nombre de tu negocio");
        nameBussiness.setRequired(true);
        nameBussiness.setRequiredIndicatorVisible(true);
        nameBussiness.focus();
        TextField nit = new TextField("NIT/ID");
        TextField phone = new TextField("Telefono");
        TextField address = new TextField("Dirección");
        TextField city = new TextField("Ciudad");
        // Listener para actualizar la vista previa del nombre
        nameBussiness.addValueChangeListener(event -> {
            String companyName = event.getValue();
            nameTenand.setText(companyName);
        });

        buffer = new MemoryBuffer();
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
//        Button btnSaveEmpresa = new Button("Guardar");

        // Configuración del listener del Upload
        upload.addSucceededListener(event -> {
            loadImageTenand(logoTenand);
        });

        Div contentTab1 = new Div(h3, text1);
        contentTab1.addClassNames("content-tab-1");

//        binder.forField(nameBussiness).bind(Tenand::getNameTenand);
        FormLayout formLayout = new FormLayout();
        formLayout.add(nameBussiness, phone, address, city, upload);
        formLayout.setColspan(nameBussiness, 2);
        formLayout.setColspan(upload, 2);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));

        Div contentformpreview = new Div();
        contentformpreview.addClassName("content-form-preview");

        contentTab1.add(contentformpreview);

        //Preview
        Div divpreview = new Div();
        divpreview.addClassName("div-preview");

        H5 h5 = new H5("Vista Previa");
        h5.addClassName(LumoUtility.TextColor.TERTIARY);

        //Bind Info
        if (binder.getBean() == null) {
            if (tenant != null) {
                binder.setBean(tenant);
            } else {
                binder.setBean(new Tenant());
            }
        }
        binder.forField(nameBussiness).bind(Tenant::getNameTenant, Tenant::setNameTenant);
        binder.forField(phone).bind(Tenant::getPhone, Tenant::setPhone);
        binder.forField(address).bind(Tenant::getAddress, Tenant::setAddress);
        binder.forField(city).bind(Tenant::getCity, Tenant::setCity);
        binder.forField(nit).bind(Tenant::getNit, Tenant::setNit);

        if (tenant != null) {
            nameBussiness.setValue(tenant.getNameTenant());
            StreamResource resource = new StreamResource("logo.png", () -> new ByteArrayInputStream(tenant.getLogo()));
            logoTenand.setSrc(resource);
        }

        divpreview.add(h5, logoTenand, nameTenand);
        contentformpreview.add(formLayout, divpreview);

        contentTab1.add(contentformpreview);
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
                grid.setItems(profileService.findByTenant(tenant));
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


        Grid<Product> grid = new Grid<>(Product.class, false);

        Div contentTab1 = new Div(h3, text1, text2, text3);
        contentTab1.addClassNames("content-tab-3");
        Button btnAddService = new Button("Agregar servicio", LineAwesomeIcon.PLUS_SQUARE.create());
        btnAddService.addThemeVariants(ButtonVariant.LUMO_SMALL);
        btnAddService.addClickListener(e -> {
            ProductForm productForm = new ProductForm(profileService);
            productForm.setEntity(new Product());
            productForm.setSaveEventListener(work_return -> {
                productService.save(work_return);
                productForm.close();
                grid.setItems(productService.findAll(Application.getTenant()));
            });
            productForm.open();
        });

        contentTab1.add(btnAddService);

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setItems(productService.findAll(Application.getTenant()));
        grid.addColumn(Product::getTitle).setHeader("Servicios");
        grid.addColumn(Product::getPrice).setHeader("Precio");
        grid.addColumn(Product::getProfiles).setHeader("Permisos");
        contentTab1.add(grid);

        return contentTab1;
    }

    private Component getTab4() {
        Div divpreview = new Div();
        divpreview.addClassName("div-preview");

        H5 h5 = new H5("Felicidades");
        h5.addClassName(LumoUtility.TextColor.TERTIARY);

        H1 nameTenand = new H1();
        logoTenandTab4 = new Image();

        //LoadInfoTenand
        if (binder.getBean() != null) {
            nameTenand.setText(binder.getBean().getNameTenant());
        }
        loadImageTenand(logoTenandTab4);

        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setMaxWidth("150px");
        vlayout.addClassNames(LumoUtility.Margin.AUTO);
        vlayout.add(createItemValid("Negocio", binder != null ? binder.getBean().getNameTenant().length() : 0));
        vlayout.add(createItemValid("Perfiles", (int) profileService.count()));
        vlayout.add(createItemValid("Servicios", (int) productService.count()));

        Button btnFinish = new Button("Guardar y Continuar", LineAwesomeIcon.CHECK_CIRCLE_SOLID.create());
        btnFinish.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnFinish.addClickListener(e -> {
            if (saveTenandInfo(binder.getBean())) {
                getUI().ifPresent(ui -> ui.navigate(WelcomeProfileView.class));
            }
        });

        divpreview.add(h5, logoTenandTab4, nameTenand, vlayout, btnFinish);
        return divpreview;
    }

    private boolean saveTenandInfo(Tenant tenand) {
        if (tenand == null || !binder.isValid() || binder.getBean().getNameTenant().isEmpty()) {
            NotificationUtil.showWarning("Debe indicar los datos del Negocio!");
            tabSheet.setSelectedIndex(0);
            return false;
        }

        try {
            if (buffer != null && buffer.getFileData() != null) {
                tenand.setLogo(buffer.getInputStream().readAllBytes());
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        try {
            binder.writeBean(tenand);
            binder.getBean().setEmail(Application.getEmailAccount());
            tenandService.save(binder.getBean());
            VaadinSession.getCurrent().setAttribute(Application.SESSION_TENANT, binder.getBean());
            NotificationUtil.showSuccess("Datos del Negocio Actualizados");
        } catch (Exception e) {
            NotificationUtil.showWarning(e.getMessage());
            return false;
        }
        return true;
    }

    private void loadImageTenand(Image logoTenand) {
        if (buffer.getFileData() == null && tenant != null) {
            StreamResource resource = new StreamResource("logo.png", () -> new ByteArrayInputStream(tenant.getLogo()));
            logoTenand.setSrc(resource);
            return;
        }
        try {
            byte[] logoBytes = null;
            logoBytes = buffer.getInputStream().readAllBytes();
            // Crea un StreamResource para mostrar la imagen
            byte[] finalLogoBytes = logoBytes;
            StreamResource resource = new StreamResource("logo.png", () -> new ByteArrayInputStream(finalLogoBytes));
            logoTenand.setSrc(resource);
            logoTenand.setVisible(true); // Muestra la vista previa del logo
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Div createItemValid(String name, Integer count) {
        Div div = new Div();
        div.addClassName("item-validation");
        div.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.ROW, LumoUtility.Gap.MEDIUM);
        Span icon = new Span();
        icon.addClassName(LumoUtility.IconSize.SMALL);
        if (count > 0) {
            icon.add(VaadinIcon.CHECK.create());
            icon.getElement().getThemeList().add("badge success");
        } else {
            icon.add(VaadinIcon.CLOSE_SMALL.create());
            icon.getElement().getThemeList().add("badge error");
        }
        div.add(icon);
        div.add(new Span(name));

        return div;
    }

    private Span createBadge(String text) {
        Span badge = new Span(text);
        badge.getElement().getThemeList().add("badge");
        return badge;
    }

}
