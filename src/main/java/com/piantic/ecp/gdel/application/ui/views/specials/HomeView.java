package com.piantic.ecp.gdel.application.ui.views.specials;

import com.piantic.ecp.gdel.application.Application;
import com.piantic.ecp.gdel.application.backend.entity.Tenant;
import com.piantic.ecp.gdel.application.backend.repository.TenandRepository;
import com.piantic.ecp.gdel.application.ui.views.MainLayout;
import com.piantic.ecp.gdel.application.ui.views.WizardConfigView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.io.ByteArrayInputStream;

@PageTitle("BarberPro | Inicio")
@Route(value = "", layout = MainLayout.class)
public class HomeView extends VerticalLayout {

    private Tenant tenant;
    private TenandRepository tenandService;

    public HomeView(TenandRepository tenandService) {
        addClassName("home-view");
        if (Application.getTenand() == null) {
            tenant = tenandService.findTenantByEmail(Application.getEmailAccount());
            if (tenant == null) {
                getUI().ifPresent(ui -> ui.navigate(WizardConfigView.class));
            } else {
                VaadinSession.getCurrent().setAttribute(Application.SESSION_TENANT, tenant);
                UI.getCurrent().navigate(MainLayout.class);
            }
        } else {
            tenant = Application.getTenand();
        }

        Div divmain = new Div();
        divmain.addClassName("div-main-content");
        divmain.setSizeFull();
        Div divpreview = new Div();
        divpreview.addClassName("div-preview");

        H5 h5 = new H5("BarberPro");
        h5.addClassName(LumoUtility.TextColor.TERTIARY);

        H1 nameTenand = new H1();
        Image logotenand = new Image();

        //LoadInfoTenand
        if (tenant != null) {
            nameTenand.setText(tenant.getNameTenant());
        }
        StreamResource resource = new StreamResource("logo.png", () -> new ByteArrayInputStream(tenant.getLogo()));
        logotenand.setSrc(resource);

        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setMaxWidth("150px");
        vlayout.addClassNames(LumoUtility.Margin.AUTO);
        vlayout.add(createItemValid("Negocio", tenant != null ? tenant.getNameTenant().length() : 0));
        vlayout.add(createItemValid("Perfiles", 1));
        vlayout.add(createItemValid("Servicios", 1));

        Button btnFinish = new Button("Asistente de Configuración", LineAwesomeIcon.HAND_POINT_UP.create());
        btnFinish.addThemeVariants(ButtonVariant.LUMO_SMALL);
        btnFinish.addClickListener(e -> {
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setHeader("Configurar");
            confirmDialog.setText("¿Ir al Asistente de Configuracion?");
            confirmDialog.setConfirmText("Aceptar");
            confirmDialog.addConfirmListener(ev -> {
                getUI().ifPresent(ui -> ui.navigate(WizardConfigView.class));
            });
            confirmDialog.open();
        });

        divpreview.add(h5, logotenand, nameTenand, vlayout, btnFinish);
        divmain.add(divpreview);

        add(divmain);

    }

    private Div createItemValid(java.lang.String name, Integer count) {
        Div div = new Div();
        div.addClassName("item-validation");
        div.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.ROW, LumoUtility.Gap.MEDIUM);
//        Span icon = new Span();
//        icon.addClassName(LumoUtility.IconSize.SMALL);

        Icon icon = VaadinIcon.CHECK.create();
        icon.addClassName(LumoUtility.IconSize.SMALL);
        if (count > 0) {
//            icon.getElement().getThemeList().add("badge success");
            icon.addClassNames(LumoUtility.TextColor.SUCCESS);
        } else {
            icon = VaadinIcon.CLOSE_SMALL.create();
            icon.addClassNames(LumoUtility.TextColor.ERROR);
//            icon.getElement().getThemeList().add("badge error");
        }

        div.add(icon);
        div.add(new Span(name));

        return div;
    }
}
