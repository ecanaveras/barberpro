package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.backend.entity.Role;
import com.piantic.ecp.gdel.application.backend.service.RoleService;
import com.piantic.ecp.gdel.application.backend.service.WorkService;
import com.piantic.ecp.gdel.application.backend.utils.NotificationUtil;
import com.piantic.ecp.gdel.application.ui.views.forms.RoleForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

@PageTitle("Roles | BarberPro")
@Route("role")
public class RoleView extends HorizontalLayout implements HasUrlParameter<Long> {

    private final TextField txtFilter;
    private final Button btnAdd;
    private final Span count = new Span();
    private Div contentRight;
    private VerticalLayout contentLeft;
    private RoleViewDetail roleviewdetail;
    private Grid<Role> grid = new Grid<>(Role.class, false);
    private RoleService roleService;
    private WorkService workService;
    private Boolean detailAdded = false;

    public RoleView(RoleService roleService, WorkService workService) {
        addClassName("role-view");
        setSizeFull();

        this.roleService = roleService;
        this.workService = workService;

        //Title
        H3 title = new H3("Roles");
        //title.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.FontSize.MEDIUM);
        count.addClassNames(LumoUtility.FontWeight.LIGHT);
        count.getElement().getThemeList().add("badge");
        HorizontalLayout contentTitle = new HorizontalLayout(title, count);
        contentTitle.setAlignSelf(Alignment.BASELINE);

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
            openFormDialog(new Role());
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

        contentLeft.add(contentTitle, toolbar, grid);

        //Content Right
        contentRight = new Div();
        contentRight.addClassName("content-right");

        add(contentLeft);
        add(contentRight);

//        System.out.println("Reloadedddd");
    }

    private void openFormDialog(Role role) {
        RoleForm formRole = new RoleForm(workService);
        formRole.setEntity(role);
        formRole.setSaveEventListener(e -> {
            this.saveRole(e);
            formRole.close();
        });
        formRole.open();
    }

    private void configureGrid() {
        grid.addClassName("role-grid");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addColumn("name")
                .setAutoWidth(true)
                .setHeader("Roles")
                .setSortable(true)
                .getStyle().set("min-width", "200px");

        createMenu();

        //TODO Configurar conlumna con los botones de editar (Servicios & Perfiles del ROLE)

        grid.asSingleSelect().addValueChangeListener(e -> showDetail(e.getValue() != null ? e.getValue().getId() : null));
    }

    private void updateList() {
        grid.setItems(roleService.findAll(txtFilter.getValue()));
        count.setText(String.valueOf(roleService.count()));
    }

    public void saveRole(Role role) {
        roleService.save(role);
        role.getWorks().forEach(work -> System.out.println(work.getId()));
        updateList();

        NotificationUtil.showSuccess("Role Guardado");
    }

    private void deleteRole(Role role) {
        ConfirmDialog confirmDialog = new ConfirmDialog("¿Eliminar a \"" + role.getName() + "\"?",
                "¿Desea borrar el registro?",
                "Eliminar", e -> {
            roleService.delete(role);
            updateList();
            getUI().ifPresent(ui -> ui.navigate(RoleView.class));
        }, "Cancelar", e -> e.getSource().close());
        confirmDialog.setConfirmButtonTheme(ButtonVariant.LUMO_ERROR.getVariantName() + " " + ButtonVariant.LUMO_PRIMARY.getVariantName());
        confirmDialog.setCloseOnEsc(true);
        confirmDialog.open();

    }

    private void createMenu() {
        GridContextMenu<Role> menu = grid.addContextMenu();
        menu.addItem(new HorizontalLayout(new Span("Nuevo"), LineAwesomeIcon.USER_PLUS_SOLID.create()), e -> openFormDialog(new Role()));
        menu.addItem("Editar", e -> openFormDialog(e.getItem().get()));
        menu.addItem("Detalles", e -> showDetail(e.getItem().get().getId()));
        menu.add(new Hr());
        GridMenuItem itemDel = menu.addItem("Eliminar", e -> deleteRole(e.getItem().get()));
        itemDel.addClassNames(LumoUtility.TextColor.ERROR);
    }

    private void showDetail(Long id) {
        if (id != null) {
            getUI().ifPresent(ui -> ui.navigate(RoleView.class, id));
        } else {
            getUI().ifPresent(ui -> ui.navigate(RoleView.class));
        }
    }


    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long id) {
        if (id != null) {
            if (roleService.findById(id) == null) {
//                beforeEvent.rerouteToError(IllegalArgumentException.class, getTranslation("role.not.found", beforeEvent.getLocation().getPath()));
//                beforeEvent.forwardTo("error");
                NotificationUtil.showContrastCloseable("Registro no encontrado...");
                return;
            }
            if (roleviewdetail != null && roleviewdetail.getRole() != null && roleviewdetail.getRole().getId().equals(id)) {
                if (contentRight.getClassNames().contains("visible")) {
                    contentLeft.removeClassName("viewing");
                    contentRight.removeClassName("visible");
                    return;
                }
            }
            if (roleviewdetail == null) {
                roleviewdetail = new RoleViewDetail(roleService, id);
                contentRight.add(roleviewdetail);
            } else {
                roleviewdetail.updateUI(id);
            }
            contentRight.addClassName("visible");
            contentLeft.addClassName("viewing");


        } else {
            if (roleviewdetail != null) {
                contentLeft.removeClassName("viewing");
                contentRight.removeClassName("visible");
            }
        }
    }

}
