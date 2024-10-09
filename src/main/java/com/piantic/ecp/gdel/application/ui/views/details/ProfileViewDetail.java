package com.piantic.ecp.gdel.application.ui.views.details;

import com.piantic.ecp.gdel.application.backend.entity.Appointment;
import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.service.ProfileService;
import com.piantic.ecp.gdel.application.backend.utils.NumberUtil;
import com.piantic.ecp.gdel.application.ui.views.ProfileView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.time.format.DateTimeFormatter;


public class ProfileViewDetail extends VerticalLayout {

    private ProfileService profileService;
    private Long id;
    private Profile profile;
    private Div content;
    Binder<Profile> binder = new Binder<>(Profile.class);

    public ProfileViewDetail(ProfileService profileService, Long id) {
        this.profileService = profileService;
        this.id = id;
        addClassNames("profile-detail-view");
//        setWidthFull();
        configInitUI();
        updateUI(id);
    }

    private void configInitUI() {
        //Header
        Header header = new Header();
        header.addClassName("view-header");
        header.addClassName(LumoUtility.Margin.Bottom.SMALL);
        header.setWidthFull();
        H3 title = new H3("Perfil - Detalles");
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
//        System.out.printf("Profile: %d\n", id);
        if (id == null) {
            return;
        }
        getData(id);
        H4 personalInfo = new H4("Información Personal");
        H4 contactInfo = new H4("Información de Contacto");
        H4 rolesInfo = new H4("Servicios Autorizados");
        H4 activityInfo = new H4("Actividad Reciente");

        if (content != null) {
            remove(content);
            content = null;
        }

        if (content == null)
            content = new Div();
        content.setWidthFull();
        content.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Gap.MEDIUM);

        content.add(personalInfo);

        content.add(cardItem(LineAwesomeIcon.USER_CIRCLE.create(), "Nombre", profile.getNameProfile()));

        content.add(contactInfo);

        content.add(cardItem(LineAwesomeIcon.PHONE_SOLID.create(), "Telefono", profile.getPhone()));
        content.add(cardItem(LineAwesomeIcon.AT_SOLID.create(), "Email", profile.getEmail()));

        content.add(rolesInfo);


        Div divservices = new Div();
        divservices.addClassName("div-services");
        divservices.addClassNames(LumoUtility.Display.FLEX
                , LumoUtility.FlexWrap.WRAP
                , LumoUtility.Gap.XSMALL);
        profileService.getProductsByProfileId(profile).forEach(product -> {
            Span spanrole = new Span(product.getTitle());
            spanrole.getElement().getThemeList().add("badge warning pill");
            divservices.add(spanrole);
        });

        content.add(divservices);

        content.add(activityInfo);

        Grid<Appointment> gridlastappoint = new Grid<>(Appointment.class, false);
        gridlastappoint.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COMPACT);
        gridlastappoint.setItems(profileService.getTenLastAppoimentsByProfileId(profile));
        gridlastappoint.addComponentColumn(appointment-> new Span(appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm"))))
                .setAutoWidth(true).setFlexGrow(1);
        gridlastappoint.addComponentColumn(appointment -> new Span(appointment.getCustomer().getName())).setAutoWidth(true);
        gridlastappoint.addComponentColumn(appointment -> new Span(NumberUtil.formatNumber(appointment.getTotal()))).setAutoWidth(true);
        if(gridlastappoint.getListDataView().getItemCount()>0){
            content.add(gridlastappoint);
        }else {
            Span span = new Span("No hay registros en este Mes");
            span.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY, LumoUtility.Padding.SMALL);
            content.add(span);
        }


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
        this.profile = profileService.findById(id);
    }

    public Profile getProfile() {
        return profile;
    }

    public void back() {
        getUI().ifPresent(ui -> ui.navigate(ProfileView.class));
    }

}
