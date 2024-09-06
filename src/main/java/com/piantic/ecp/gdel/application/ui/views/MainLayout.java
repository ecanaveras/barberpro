package com.piantic.ecp.gdel.application.ui.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
@Route("")
public class MainLayout extends AppLayout {

    private H1 viewTitle;
    private Span count;

    public MainLayout() {
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

        nav.addItem(new SideNavItem("Area de Trabajo", WorkingView.class, LineAwesomeIcon.FIRE_ALT_SOLID.create()));
        nav.addItem(new SideNavItem("Actividad", ActivityView.class, LineAwesomeIcon.HEARTBEAT_SOLID.create()));
        nav.addItem(new SideNavItem("Clientes", CustomerView.class, LineAwesomeIcon.ADDRESS_BOOK.create()));

        SideNavItem adminsection = new SideNavItem("Admin");
        adminsection.setPrefixComponent(LineAwesomeIcon.COG_SOLID.create());
        adminsection.addItem(new SideNavItem("Servicios", WorkView.class, LineAwesomeIcon.MAGIC_SOLID.create()));
        adminsection.addItem(new SideNavItem("Perfiles", ProfileView.class, LineAwesomeIcon.USER.create()));
        adminsection.addItem(new SideNavItem("Roles", RoleView.class, LineAwesomeIcon.OBJECT_GROUP.create()));

        nav.addItem(adminsection);

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
        count.setText(getCurrentRowsPage());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    public String getCurrentRowsPage() {
        if(getContent() instanceof ActivityView){
//            return String.valueOf(((ActivityView) getContent()).act.count());
        }

        if(getContent() instanceof CustomerView){
            return String.valueOf(((CustomerView) getContent()).customerService.count());
        }

        if(getContent() instanceof ProfileView){
            return String.valueOf(((ProfileView) getContent()).profileService.count());
        }

        if(getContent() instanceof RoleView){
            return String.valueOf(((RoleView) getContent()).roleService.count());
        }

        if(getContent() instanceof WorkView){
            return String.valueOf(((WorkView) getContent()).workService.count());
        }

        return "";
    }
}
