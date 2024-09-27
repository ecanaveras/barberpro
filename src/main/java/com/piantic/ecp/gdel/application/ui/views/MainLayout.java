package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.Application;
import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.entity.Tenant;
import com.piantic.ecp.gdel.application.backend.service.TenandService;
import com.piantic.ecp.gdel.application.ui.views.specials.SelectProfileView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
@Route("main")
public class MainLayout extends AppLayout {

    private Tenant tenant;
    private H1 viewTitle;
    private Span count;

    public MainLayout(TenandService tenandService) {
        addClassName("main-layout");

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();

    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.Right.SMALL);
        count = new Span();
        count.addClassNames(LumoUtility.FontWeight.LIGHT);
        count.getElement().getThemeList().add("badge");

        addToNavbar(true, toggle, viewTitle, count);
    }

    private void addDrawerContent() {
        Span appName = new Span("BarberPro");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        nav.addItem(new SideNavItem("Inicio", HomeView.class, LineAwesomeIcon.HOME_SOLID.create()));
        nav.addItem(new SideNavItem("Mi Negocio", DashboardView.class, LineAwesomeIcon.CHART_AREA_SOLID.create()));
        if(Application.getModeApp()==1) {
            nav.addItem(new SideNavItem("Area de Trabajo", SelectProfileView.class, LineAwesomeIcon.FIRE_ALT_SOLID.create()));
        }else {
            nav.addItem(new SideNavItem("Area de Trabajo", WorkingView.class, LineAwesomeIcon.FIRE_ALT_SOLID.create()));
        }
        nav.addItem(new SideNavItem("Actividad", ActivityView.class, LineAwesomeIcon.HEARTBEAT_SOLID.create()));
        nav.addItem(new SideNavItem("Clientes", CustomerView.class, LineAwesomeIcon.ADDRESS_BOOK.create()));

        SideNavItem adminsection = new SideNavItem("Admin");
        adminsection.setExpanded(true);
        adminsection.setPrefixComponent(LineAwesomeIcon.COG_SOLID.create());
        adminsection.addItem(new SideNavItem("Servicios", ProductView.class, LineAwesomeIcon.MAGIC_SOLID.create()));
        adminsection.addItem(new SideNavItem("Perfiles", ProfileView.class, LineAwesomeIcon.USER.create()));
//        adminsection.addItem(new SideNavItem("Roles", RoleView.class, LineAwesomeIcon.OBJECT_GROUP.create()));

        nav.addItem(adminsection);

        return nav;
    }


    private Footer createFooter() {
        Footer layout = new Footer();
        Profile profile = (Profile) VaadinSession.getCurrent().getAttribute(Application.SESSION_PROFILE);
        if (profile == null) {
            profile = new Profile();
            profile.setNameProfile("MODO ASISITIDO");
        }

        Div divencabezado = new Div();
        divencabezado.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Gap.SMALL, LumoUtility.AlignItems.CENTER);

        Avatar avatarprofile = new Avatar(profile.getNameProfile());

        Span nameprofile = new Span(profile.getNameProfile());
        nameprofile.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.TextColor.BODY, LumoUtility.FontSize.SMALL);

        Span activeprofile = new Span("Activo");
        activeprofile.getElement().getThemeList().add("badge success");

        Div divprofilename = new Div(nameprofile);
        divprofilename.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.AlignItems.START, LumoUtility.Gap.XSMALL);
        divprofilename.add(nameprofile, activeprofile);

        divencabezado.add(avatarprofile, divprofilename);


        Avatar avatarprofile2 = new Avatar(profile.getNameProfile());
        MenuBar menu = new MenuBar();
        menu.addThemeVariants(MenuBarVariant.LUMO_TERTIARY, MenuBarVariant.LUMO_SMALL);
        MenuItem avataritem = menu.addItem(avatarprofile2);
        SubMenu subMenu = avataritem.getSubMenu();
        subMenu.addItem(divencabezado).setEnabled(false);
        subMenu.add(new Hr());
        subMenu.addItem(LineAwesomeIcon.USER.create(), e -> {
        }).add(new Text("Perfil"));
        subMenu.addItem(LineAwesomeIcon.TOOLS_SOLID.create(), e -> {
        }).add(new Text("Preferencias"));
        MenuItem tema = subMenu.addItem(LineAwesomeIcon.SUN.create());
        tema.add(new Text("Tema"));
        SubMenu subMenu1 = tema.getSubMenu();
        subMenu1.addItem(LineAwesomeIcon.SUN.create(), e -> {
            UI.getCurrent().getElement().getThemeList().remove("dark");
        }).add(new Text("Claro"));
        subMenu1.addItem(LineAwesomeIcon.MOON.create(), e -> {
            UI.getCurrent().getElement().getThemeList().add("dark");
        }).add(new Text("Oscuro"));
        subMenu.add(new Hr());
        subMenu.addItem(LineAwesomeIcon.SIGN_OUT_ALT_SOLID.create(), event -> {
            VaadinSession.getCurrent().setAttribute(Application.SESSION_PROFILE, null);
            VaadinSession.getCurrent().setAttribute(Application.SESSION_MODE_APP, null);
            getUI().ifPresent(ui -> ui.navigate(WelcomeModeView.class));
        }).add(new Text("Salir"));

        layout.add(menu);

        return layout;
    }


    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        if (Application.getModeApp() == 2 && Application.getProfile() == null) {
            getUI().ifPresent(ui -> ui.navigate(WelcomeProfileView.class));
        }
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
        count.setText(getCurrentRowsPage());
    }

    private java.lang.String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    public java.lang.String getCurrentRowsPage() {
        if (getContent() instanceof ActivityView) {
//            return String.valueOf(((ActivityView) getContent()).act.count());
        }

        if (getContent() instanceof CustomerView) {
            return java.lang.String.valueOf(((CustomerView) getContent()).customerService.count());
        }

        if (getContent() instanceof ProfileView) {
            return java.lang.String.valueOf(((ProfileView) getContent()).profileService.count());
        }

        if (getContent() instanceof RoleView) {
            return java.lang.String.valueOf(((RoleView) getContent()).roleService.count());
        }

        if (getContent() instanceof ProductView) {
            return java.lang.String.valueOf(((ProductView) getContent()).productService.count());
        }

        return "";
    }
}
