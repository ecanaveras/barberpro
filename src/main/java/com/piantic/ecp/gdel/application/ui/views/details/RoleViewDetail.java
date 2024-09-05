package com.piantic.ecp.gdel.application.ui.views.details;

import com.piantic.ecp.gdel.application.backend.entity.Role;
import com.piantic.ecp.gdel.application.backend.service.ProfileService;
import com.piantic.ecp.gdel.application.backend.service.RoleService;
import com.piantic.ecp.gdel.application.ui.views.RoleView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;


public class RoleViewDetail extends VerticalLayout {

    private RoleService roleService;
    private ProfileService profileService;
    private final Long id;
    private Role role;
    private Div content;
    Binder<Role> binder = new Binder<>(Role.class);

    public RoleViewDetail(RoleService roleService, ProfileService profileService, Long id) {
        addClassNames("role-detail-view");
        this.roleService = roleService;
        this.profileService = profileService;
        this.id = id;

        configInitUI();
        updateUI(id);
    }

    private void configInitUI() {
        //Header
        Header header = new Header();
        header.addClassName("view-header");
        header.addClassName(LumoUtility.Margin.Bottom.SMALL);
        header.setWidthFull();
        H3 title = new H3("Role - Detalles");
        title.addClassNames(LumoUtility.FontWeight.SEMIBOLD);
        Button btnBack = new Button(LineAwesomeIcon.ARROW_LEFT_SOLID.create(), e -> back());
        btnBack.addClassNames("back-button");
        btnBack.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);
//        btnBack.addClassNames(LumoUtility.Margin.Left.AUTO);
        header.add(btnBack);
        header.add(title);

        add(header);
    }

    public void updateUI(Long id) {
//        System.out.printf("Role: %d\n", id);
        if (id == null) {
            return;
        }
        getData(id);
        H4 info1 = new H4("Perfiles Asignados");
        H4 info2 = new H4("Servicios Asignados");

        if (content != null) {
            remove(content);
            content = null;
        }

        if (content == null)
            content = new Div();
        content.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Gap.MEDIUM);
        content.setWidthFull();

        content.add(cardItem(LineAwesomeIcon.OBJECT_GROUP.create(), "Nombre del Role", role.getName()));

        content.add(info1);

        Div divprofiles = new Div();
        divprofiles.addClassNames(LumoUtility.Display.FLEX,
                LumoUtility.Gap.Column.SMALL,
                LumoUtility.Gap.Row.SMALL,
                LumoUtility.FlexWrap.WRAP);
        divprofiles.addClassName("div-services-profile");
        profileService.getProfileByRoleId(id).forEach(profile -> {
            Span service = new Span(profile.getNameProfile());
            service.getElement().getThemeList().add("badge contrast");
            divprofiles.add(service);
        });

        content.add(divprofiles);

        content.add(info2);

        Div divservices = new Div();
        divservices.addClassNames(LumoUtility.Display.FLEX,
                LumoUtility.Gap.Column.SMALL,
                LumoUtility.Gap.Row.SMALL,
                LumoUtility.FlexWrap.WRAP);
        divservices.addClassName("div-services-role");
        roleService.getWorksByRoleId(id).forEach(work -> {
            Span service = new Span(work.getTitle());
            service.getElement().getThemeList().add("badge warning");
            divservices.add(service);
        });

        content.add(divservices);

        add(content);
    }


    public Div cardItem(SvgIcon iconInfo, String label_, String data) {
        Div divItem = new Div();
        divItem.addClassName("div-content-item");
        divItem.addClassName(LumoUtility.Gap.MEDIUM);
        Div divIcon = new Div();
        divIcon.addClassName("icon-item");

        divIcon.add(iconInfo);

        H5 label = new H5(label_);
        label.addClassNames(LumoUtility.Margin.NONE);

        Span info = new Span(data);

        divItem.add(divIcon, new Div(label, info));
        return divItem;
    }

    private void getData(Long id) {
        this.role = roleService.findById(id);
    }

    public Role getRole() {
        return role;
    }

    public void back() {
        getUI().ifPresent(ui -> ui.navigate(RoleView.class));
    }

}
