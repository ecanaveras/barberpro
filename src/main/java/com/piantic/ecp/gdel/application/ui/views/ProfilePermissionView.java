package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.entity.Role;
import com.piantic.ecp.gdel.application.backend.entity.Work;
import com.piantic.ecp.gdel.application.backend.service.ProfileService;
import com.piantic.ecp.gdel.application.backend.service.RoleService;
import com.piantic.ecp.gdel.application.backend.utils.NotificationUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.ArrayList;
import java.util.Set;

@PageTitle("Permisos del Perfil")
@Route(value = "permission", layout = MainLayout.class)
public class ProfilePermissionView extends VerticalLayout implements HasUrlParameter<Long> {

    private ProfileService profileService;
    private RoleService roleService;

    ComboBox<Profile> profileComboBox;
    Grid<Role> roleGrid = new Grid<>(Role.class, false);

    TextField nameProfile = new TextField("Nombre Perfil");
    Button btnSave = new Button("Guardar");
    Button cancel = new Button("Cancelar");

    Binder<Profile> binder = new Binder<>(Profile.class);

    Profile updateitem;

    public ProfilePermissionView(ProfileService profileService, RoleService roleService) {
        this.profileService = profileService;
        this.roleService = roleService;

        profileComboBox = new ComboBox<>("Perfiles");
        profileComboBox.setAllowCustomValue(false);
        profileComboBox.setItems(profileService.findAll());
        profileComboBox.setItemLabelGenerator(Profile::getNameProfile);
        profileComboBox.addValueChangeListener(event -> {
            getUI().ifPresent(ui -> ui.navigate(ProfilePermissionView.class, event.getValue().getId()));
        });

        binder.bindInstanceFields(this);
        nameProfile.setVisible(false);
        add(nameProfile);
        binder.forField(nameProfile).bind(Profile::getNameProfile, Profile::setNameProfile);

        binder.addStatusChangeListener(e -> btnSave.setEnabled(binder.isValid()));

        //Note
        Button clearButton = new Button(VaadinIcon.CLOSE_SMALL.create());
        clearButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST,
                ButtonVariant.LUMO_TERTIARY_INLINE);
        clearButton.getStyle().set("margin-inline-start",
                "var(--lumo-space-xs)");
        Span note = new Span(new Span(LineAwesomeIcon.INFO_CIRCLE_SOLID.create()));
        note.setClassName("note");
        note.getElement().getThemeList().add("badge contrast");
        note.addClassNames(LumoUtility.Padding.XLARGE,
                LumoUtility.Border.ALL);
        note.add(new Span("Seleccione el perfil, y luego asigne uno o más roles, tenga en cuenta que los servicios están relacionados directamente a los roles"));
        note.add(clearButton);

        clearButton.addClickListener(e -> {
            note.getElement().removeFromParent();
        });

        add(note);

        add(profileComboBox);
        configureGrids();
        add(roleGrid);

        //Btns
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.setEnabled(false);
        btnSave.addClickListener(e -> {
            binder.getBean().setRoles(roleGrid.getSelectedItems());
            profileService.save(binder.getBean());
            NotificationUtil.showSuccess("Permisos actualizados");
        });

        cancel.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate(ProfileView.class));
        });

        add(new HorizontalLayout(btnSave, cancel));
    }

    private void configureGrids() {
        roleGrid.setItems(new ArrayList<>());
        roleGrid.setItems(roleService.findAll());
        roleGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        roleGrid.addColumn("name").setHeader("Roles");
        roleGrid.addComponentColumn(role -> getSpanWorkItem(role.getWorks())).setHeader("Servicios Asignados");
        updateInfoGrid();
    }

    /**
     * Crea una coleción de badges
     * @param works
     * @return
     */
    private Div getSpanWorkItem(Set<Work> works) {
        Div divworks = new Div();
        divworks.addClassNames(LumoUtility.Display.FLEX,
                LumoUtility.Gap.Column.SMALL,
                LumoUtility.Gap.Row.SMALL,
                LumoUtility.FlexWrap.WRAP);
        works.forEach(work -> {
            Span spanw = new Span(work.getTitle());
            spanw.getElement().getThemeList().add("badge warning");
            divworks.add(spanw);
        });
        return divworks;
    }

    private void updateInfoGrid() {
        if (updateitem != null) {
            roleGrid.deselectAll();
            updateitem.getRoles().forEach(role -> roleGrid.select(role));
        }
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long idProfile) {
        if (idProfile != null) {
            updateitem = profileService.findById(idProfile);
            binder.setBean(updateitem);
            profileComboBox.setValue(updateitem);
            updateInfoGrid();
        }
    }
}
