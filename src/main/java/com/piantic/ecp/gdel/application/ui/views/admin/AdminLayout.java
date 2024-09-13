package com.piantic.ecp.gdel.application.ui.views.admin;

import com.piantic.ecp.gdel.application.ui.views.MainLayout;
import com.piantic.ecp.gdel.application.ui.views.ProfileView;
import com.piantic.ecp.gdel.application.ui.views.RoleView;
import com.piantic.ecp.gdel.application.ui.views.ProductView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

@Route(value = "admin", layout = MainLayout.class)
public class AdminLayout extends AppLayout {

    public AdminLayout() {
//        setPrimarySection(Section.DRAWER);
        addHeaderContent();
        addDrawerContent();

        Div contentDiv = new Div();
        Paragraph viewContent = new Paragraph("Administre los perfiles, servicios y roles que su negocio necesite");
        contentDiv.add(viewContent);

        setContent(contentDiv);
    }

    private void addHeaderContent() {
        HorizontalLayout header = new HorizontalLayout();
        header.addClassNames(LumoUtility.Display.FLEX,
                LumoUtility.Width.FULL,
                LumoUtility.AlignItems.CENTER, LumoUtility.Padding.SMALL);
        header.add(LineAwesomeIcon.COG_SOLID.create(), new H5("Admin"));
        header.add(getButtonBack());

        addToNavbar(header);
        addToNavbar(true, createNavigationHeader());
    }

    private Button getButtonBack() {
        Button btnBack = new Button(LineAwesomeIcon.WINDOW_CLOSE.create());
        btnBack.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_CONTRAST);
        btnBack.getStyle().set("margin-left", "auto");
        btnBack.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate(MainLayout.class));
        });
        return btnBack;
    }

    private void addDrawerContent() {
        Span appName = new Span("Admin");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.MEDIUM);
        Header header = new Header(appName, getButtonBack());
        header.addClassName(LumoUtility.Display.FLEX);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller);
    }

    /**
     * Navegacion Standar Lateral
     * @return
     */
    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        nav.addItem(new SideNavItem("Servicios", ProductView.class, LineAwesomeIcon.TAGS_SOLID.create()));
        nav.addItem(new SideNavItem("Perfiles", ProfileView.class, LineAwesomeIcon.ADDRESS_CARD.create()));
        nav.addItem(new SideNavItem("Roles", RoleView.class, LineAwesomeIcon.ICONS_SOLID.create()));

        return nav;
    }

    /**
     * Navegacion Personalizada en el "Footer"
     * @return
     */
    private HorizontalLayout createNavigationHeader() {
        HorizontalLayout navigation = new HorizontalLayout();
        navigation.addClassNames(LumoUtility.Width.FULL,
                LumoUtility.JustifyContent.EVENLY,
                LumoUtility.AlignSelf.STRETCH);
        navigation.setPadding(false);
        navigation.setSpacing(false);
        navigation.add(
                createLink(LineAwesomeIcon.MAGIC_SOLID, "Servicios", ProductView.class),
                createLink(LineAwesomeIcon.USER, "Perfiles", ProfileView.class),
                createLink(LineAwesomeIcon.OBJECT_GROUP,"Roles", RoleView.class)
        );
        return navigation;
    }

    private RouterLink createLink(LineAwesomeIcon icon, String viewName, Class classtarget){
        RouterLink link = new RouterLink();
        link.setRoute(classtarget);
        link.addClassNames(LumoUtility.Display.FLEX,
                LumoUtility.AlignItems.CENTER,
                LumoUtility.Padding.Horizontal.LARGE,
                LumoUtility.TextColor.SECONDARY);
        link.add(icon.create());
        link.getElement().setAttribute("aria-label", viewName);
        return link;
    }

}
