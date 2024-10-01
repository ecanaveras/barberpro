package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.service.ProfileService;
import com.piantic.ecp.gdel.application.backend.service.RoleService;
import com.piantic.ecp.gdel.application.backend.utils.NotificationUtil;
import com.piantic.ecp.gdel.application.ui.views.details.ProfileViewDetail;
import com.piantic.ecp.gdel.application.ui.views.forms.ProfileForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

@PageTitle("Perfiles")
@Route(value = "profile", layout = MainLayout.class)
public class ProfileView extends HorizontalLayout implements HasUrlParameter<Long> {

    private final TextField txtFilter;
    private final Button btnAdd;
    private final Span count = new Span();

    private Div contentRight;
    private VerticalLayout contentLeft;
    private ProfileViewDetail profileViewDetail;
    private Grid<Profile> grid = new Grid<>(Profile.class, false);
    public ProfileService profileService;
    private Boolean detailAdded = false;

    public ProfileView(ProfileService profileService, RoleService roleService) {
        addClassName("profile-view");
        setSizeFull();

        this.profileService = profileService;

        //Toolbar
        txtFilter = new TextField();
        txtFilter.setClearButtonVisible(true);
        txtFilter.setPlaceholder("Filtrar...");
        txtFilter.setValueChangeMode(ValueChangeMode.LAZY);
        txtFilter.addValueChangeListener(e -> updateList());
        txtFilter.addClassNames(LumoUtility.Flex.AUTO);
        txtFilter.setMaxWidth("26rem");

        btnAdd = new Button(LineAwesomeIcon.USER_PLUS_SOLID.create());
        btnAdd.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnAdd.addClickListener(click -> {
            openFormDialog(new Profile());
        });

        HorizontalLayout toolbar = new HorizontalLayout(txtFilter, btnAdd);
        toolbar.setWidthFull();
        toolbar.setFlexGrow(0);

        //Grid
        configureGrid();

        updateList();

        //Content Left
        contentLeft = new VerticalLayout();
        contentLeft.addClassName("content-left");
        contentLeft.setSizeFull();

        contentLeft.add(toolbar, grid);

        //Content Right
        contentRight = new Div();
        contentRight.addClassName("content-right");

        add(contentLeft);
        add(contentRight);
    }

    private void openFormDialog(Profile profile) {
        ProfileForm profileForm = new ProfileForm();
        profileForm.setEntity(profile);
        profileForm.setSaveEventListener(e -> {
            this.saveProfile(e);
            profileForm.close();
        });
        profileForm.open();
    }

    private void configureGrid() {
        grid.addClassName("profile-grid");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addComponentColumn(profile ->
                new H5(profile.getNameProfile())
        ).setKey("name1").setAutoWidth(true).setHeader("Perfil").setSortable(true).setComparator(Profile::getNameProfile).getStyle().set("min-width", "200px");
//        grid.addColumn(Profile::getStatus).setHeader("Estado").setSortable(true);
        grid.addComponentColumn(profile -> {
            Span span = new Span(profile.getStatus().toString());
            if (profile.getStatus().equals(Profile.Status.Activo)) {
                span.getElement().getThemeList().add("badge success");
            } else {
                span.getElement().getThemeList().add("badge error");
            }
            return span;
        }).setHeader("Estado").setSortable(true).setComparator(Profile::getStatus);
        grid.addColumn(Profile::getPhone).setHeader("Telefono");
        grid.addColumn(Profile::getEmail).setHeader("Email");
//        grid.addComponentColumn(profile -> getSpanWorkItem(profile.getRoles())).setHeader("Roles Asignados");

        createMenu();

        grid.asSingleSelect().addValueChangeListener(e -> showDetail(e.getValue() != null ? e.getValue().getId() : null));
    }

    private void updateList() {
        grid.setItems(profileService.findAll(txtFilter.getValue()));
        count.setText(String.valueOf(profileService.count()));
    }

    public void saveProfile(Profile profile) {
        profileService.save(profile);
        updateList();
        NotificationUtil.showSuccess("Perfil Guardado!");
    }

    private void deleteProfile(Profile profile) {
        ConfirmDialog confirmDialog = new ConfirmDialog("¿Eliminar a \"" + profile.getNameProfile() + "\"?",
                "¿Desea borrar el registro?",
                "Eliminar", e -> {
            profile.setEnabled(false);
            profileService.save(profile);
            updateList();
            getUI().ifPresent(ui -> ui.navigate(ProfileView.class));
        }, "Cancelar", e -> e.getSource().close());
        confirmDialog.setConfirmButtonTheme(ButtonVariant.LUMO_ERROR.getVariantName() + " " + ButtonVariant.LUMO_PRIMARY.getVariantName());
        confirmDialog.setCloseOnEsc(true);
        confirmDialog.open();

    }

    private void createMenu() {
        GridContextMenu<Profile> menu = grid.addContextMenu();
        menu.addItem(new HorizontalLayout(new Span("Nuevo"), LineAwesomeIcon.USER_PLUS_SOLID.create()), e -> openFormDialog(new Profile()));
        menu.addItem("Editar", e -> openFormDialog(e.getItem().get()));
        menu.addItem("Detalles", e -> showDetail(e.getItem().get().getId()));
        menu.add(new Hr());
        GridMenuItem itemDel = menu.addItem("Eliminar", e -> deleteProfile(e.getItem().get()));
        itemDel.addClassNames(LumoUtility.TextColor.ERROR);
    }

    private void showDetail(Long id) {
        if (id != null) {
            getUI().ifPresent(ui -> ui.navigate(ProfileView.class, id));
        } else {
            getUI().ifPresent(ui -> ui.navigate(ProfileView.class));
        }
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long id) {
        if (id != null) {
            if (profileService.findById(id) == null) {
//                beforeEvent.rerouteToError(IllegalArgumentException.class, getTranslation("profile.not.found", beforeEvent.getLocation().getPath()));
//                beforeEvent.forwardTo("error");
                NotificationUtil.showContrastCloseable("Registro no encontrado...");
                return;
            }
            if (profileViewDetail != null && profileViewDetail.getProfile() != null && profileViewDetail.getProfile().getId().equals(id)) {
                if(contentRight.getClassNames().contains("visible")) {
                    contentLeft.removeClassName("viewing");
                    contentRight.removeClassName("visible");
                    return;
                }
            }
            if (profileViewDetail == null) {
                profileViewDetail = new ProfileViewDetail(profileService, id);
                contentRight.add(profileViewDetail);
            } else {
                profileViewDetail.updateUI(id);
            }
            contentRight.addClassName("visible");
            contentLeft.addClassName("viewing");


        } else {
            if (profileViewDetail != null) {
                contentLeft.removeClassName("viewing");
                contentRight.removeClassName("visible");
            }
        }
    }

}
