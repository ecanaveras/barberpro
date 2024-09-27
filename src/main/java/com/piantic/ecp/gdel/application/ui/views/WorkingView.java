package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.Application;
import com.piantic.ecp.gdel.application.backend.entity.Customer;
import com.piantic.ecp.gdel.application.backend.entity.Product;
import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.service.AppointmentService;
import com.piantic.ecp.gdel.application.backend.service.CustomerService;
import com.piantic.ecp.gdel.application.backend.service.ProductService;
import com.piantic.ecp.gdel.application.ui.views.dialog.FindDialogView;
import com.piantic.ecp.gdel.application.ui.views.specials.SelectProfileView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.validation.constraints.NotNull;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Trabajando")
@Route(value = "working", layout = MainLayout.class)
public class WorkingView extends VerticalLayout implements BeforeEnterObserver, BeforeLeaveObserver {

    CustomerService customerService;
    ProductService productService;
    AppointmentService appointmentService;

    Grid<WorkAdded> gridservices = new Grid<>(WorkAdded.class, false);
    Grid<WorkAdded> gridsales = new Grid<>(WorkAdded.class, false);

    Div divtoolbar = new Div();
    Div divleft = new Div();
    Div divright = new Div();
    Div divsummary = new Div();
    Div divfinal = new Div();
    Span total = new Span();

    WorkAdded dragitem;

    GridListDataView<WorkAdded> dataviewleft = gridservices.getListDataView();
    GridListDataView<WorkAdded> dataviewright = gridsales.getListDataView();

    Profile profileworking;

    private Integer modeApp = 1;

//    Editor<WorkAdded> editorsales = gridsales.getEditor();

    //Preferents
    private boolean showbuttons = false;
    private Button btnContinuar;
    private Button btnLimpiar;

    public WorkingView(ProductService productService, CustomerService customerService, AppointmentService appointmentService) {
        addClassName("working-view-main");
        setSizeFull();

        this.productService = productService;
        this.customerService = customerService;
        this.appointmentService = appointmentService;

        //Modo APP
        modeApp = Application.getModeApp();

        //Profile
        profileworking = Application.getProfile();
        if (profileworking == null) {
            if (modeApp == 1) {
                getUI().ifPresent(ui -> ui.navigate(SelectProfileView.class));
            } else {
                getUI().ifPresent(ui -> ui.navigate(WelcomeProfileView.class));
            }
        }


        divleft.addClassName("div-left");
        divright.addClassName("div-right");
        divsummary.addClassName("div-summary");

        divfinal.addClassName("div-final");

        configureGrids();

        dataviewright.addItemCountChangeListener(event -> refreshComponents());

        divleft.add(gridservices);

        divright.add(gridsales);
        divright.add(divsummary);

        divsummary.add(new H3("Total: $"), total);

        configureToolbar();

        Div content = new Div();
        content.addClassName("working-view-content");

        content.add(divleft, divright, divfinal);

        //Title

        H3 title = new H3();
        Button btnChangeProfile = new Button(LineAwesomeIcon.EXCHANGE_ALT_SOLID.create(), event -> UI.getCurrent().navigate(SelectProfileView.class));
        btnChangeProfile.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        btnChangeProfile.setTooltipText("Cambiar perfil");
        btnChangeProfile.setVisible(false);
        if (modeApp == 2) {
            title.setText(String.format("%s", profileworking.getNameProfile()));
            UI.getCurrent().getPage().setTitle("Trabajando: MODO PERFIL");
        } else {
            btnChangeProfile.setVisible(true);
            UI.getCurrent().getPage().setTitle("Trabajando: MODO ASISTIDO");
            title.setText(String.format("%s", profileworking.getNameProfile()));
        }
        //title.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.FontSize.MEDIUM);
        HorizontalLayout contentTitle = new HorizontalLayout(title, btnChangeProfile, divtoolbar);
        contentTitle.addClassName("content-title");
        contentTitle.setWidthFull();
        add(contentTitle, content);

    }

    private void configureToolbar() {
        divtoolbar.setWidthFull();
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidthFull();
        toolbar.addClassName("toolbar");
        btnContinuar = new Button("Guardar", LineAwesomeIcon.SAVE.create());
        btnContinuar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnContinuar.addThemeVariants(ButtonVariant.LUMO_ICON);
        btnContinuar.addClickListener(event -> {
            FindDialogView<Customer> findCustomer =
                    new FindDialogView<>(customerService,
                            Customer.class,
                            "name");
            findCustomer.setHeaderTitle("Clientes");
            findCustomer.setResizable(true);
            findCustomer.setNamesColumnFilter(List.of("name"));
            findCustomer.setLabeslColumnFilter(List.of("Cliente"));
            findCustomer.configureGrid();
            findCustomer.addDetachListener(detachEvent -> {
                if (findCustomer.getIdSelected() != null) {
                    WorkFinishView vfinal = new WorkFinishView(customerService, appointmentService, findCustomer.getIdSelected(), dataviewright.getItems(), getTotalWorked());
                    vfinal.setCloseEventListener(() -> {
                        controlShowViews(false);
                        if (vfinal.isSaved()) {
                            resetWorkspace();
                            if (Application.getModeApp() == 1) {
                                getUI().ifPresent(ui -> ui.navigate(SelectProfileView.class));
                            }
                        }
                    });
                    divfinal.add(vfinal);
                    controlShowViews(true);
                }
            });

            findCustomer.open();

        });

        btnLimpiar = new Button("Reiniciar", LineAwesomeIcon.UNDO_ALT_SOLID.create());
        btnLimpiar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnLimpiar.addThemeVariants(ButtonVariant.LUMO_ICON);
        btnLimpiar.addClickListener(e -> {
            //Limpiar zona de trabajo
            ConfirmDialog confirmDialog = new ConfirmDialog("Aviso!", "¿Seguro quieres empezar de Nuevo?", "Cancelar", event -> {
            });
            confirmDialog.setCloseOnEsc(true);
            confirmDialog.setRejectable(true);
            confirmDialog.setRejectText("Sí, Reiniciar");
            confirmDialog.addRejectListener(ev -> {
                resetWorkspace();
            });
            confirmDialog.open();
        });

        toolbar.add(btnContinuar, btnLimpiar, getProgreso());
        divtoolbar.add(toolbar);
    }


    private void configureGrids() {
        //Disponibles
        loadDataGridServices();
        gridservices.setRowsDraggable(true);
        gridservices.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        gridservices.addComponentColumn(service -> {
            Div itemservice = new Div();
            itemservice.addClassName("item-service");
            return getIconItem(service.getServicio(), itemservice);
        });
        gridservices.addComponentColumn(service -> service.getCant() > 0 ? markIcon() : new Span("")).setWidth("60px").setFlexGrow(0);
        gridservices.addColumn(
                new ComponentRenderer<>(Button::new, (btn, work_item) -> {
                    btn.addClassName("btn-add-item");
                    btn.setIcon(LineAwesomeIcon.PLUS_SOLID.create());
                    btn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_CONTRAST);
                    btn.addClassNames(LumoUtility.Padding.XSMALL, LumoUtility.Margin.XSMALL);
                    //Agregar el elemento
                    btn.addClickListener(e -> addServiceGrid(work_item));
                })
        ).setAutoWidth(true).setFlexGrow(0);

        //Double Touch
        gridservices.addItemDoubleClickListener(e -> addServiceGrid(e.getItem()));


        //Drag and Drog
        gridservices.addDragStartListener(e -> {
            dragitem = e.getDraggedItems().get(0);
            gridservices.setDropMode(GridDropMode.ON_GRID);
            gridsales.setDropMode(GridDropMode.ON_GRID);
        });
        gridservices.addDragEndListener(e -> {
            dragitem = null;
            gridservices.setDropMode(null);
            gridsales.setDropMode(null);
        });


        //Vendidos
        gridsales.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        gridsales.setRowsDraggable(true);
        gridsales.addComponentColumn(service -> {
            Div itemservice = new Div();
            itemservice.addClassNames("item-service", "added");
            return getIconItem(service.getServicio(), itemservice);
        });
        gridsales.addComponentColumn(this::createForm).setAutoWidth(true).setFlexGrow(0);
        gridsales.addColumn(
                new ComponentRenderer<>(Button::new, (btn, work_item) -> {
                    btn.addClassName("btn-delete-item");
                    btn.setIcon(LineAwesomeIcon.TRASH_ALT.create());
                    btn.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
                    //Elimina el elemento
                    btn.addClickListener(e -> {
                        dragitem = work_item;
                        deleteServiceGrid();
                    });
                })
        ).setKey("action").setAutoWidth(true).setFlexGrow(0).setVisible(showbuttons);

        /*
        Grid.Column<WorkAdded> deleteColum = gridsales.addComponentColumn(workAdded ->{
            return new Span("");
        });
        deleteColum.setFlexGrow(0).setFrozenToEnd(true).setAutoWidth(true);

        //BtnDeleteRow
        Button btn = new Button(LineAwesomeIcon.TRASH_ALT.create());
        btn.addClassName("btn-delete-item");
        btn.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
        //Elimina el elemento
        btn.addClickListener(e -> {
            dragitem = editorsales.getItem();
            deleteServiceGrid();
        });

        gridsales.asSingleSelect().addValueChangeListener(event -> {
            if(editorsales.isOpen()){
                editorsales.cancel();
            }
            gridsales.getEditor().editItem(event.getValue());
            deleteColum.setEditorComponent(btn);
        });*/

        gridsales.addDropListener(e -> addServiceGrid(dragitem));


    }

    /**
     * Carga los servicios disponibles para el perfil
     */
    private void loadDataGridServices() {
        if (profileworking != null) {
            List<Product> productList = productService.findProductsByProfile(Application.getTenant(), profileworking);
            if (productList.isEmpty()) {
                ConfirmDialog confirmDialog = new ConfirmDialog();
                confirmDialog.setCloseOnEsc(true);
                confirmDialog.setHeader("Aviso");
                confirmDialog.setText("No hemos encontrado Servicios activos\n¿Deseas agregar nuevo Servicios?");
                confirmDialog.addConfirmListener(event -> getUI().ifPresent(ui -> ui.navigate(ProductView.class)));
                confirmDialog.setConfirmText("Aceptar");
                confirmDialog.open();
            }
            gridservices.setItems(productList.stream().map(work -> {
                WorkAdded service = new WorkAdded();
                service.setServicio(work);
                return service;
            }).collect(Collectors.toList()));
        }
    }

    private void addServiceGrid(WorkAdded work) {
//        dragitem.setAdded(true);
        dragitem = work;
        dragitem.setCant(dragitem.getCant() + 1);
        dataviewright.addItem(dragitem);
        dataviewleft.refreshItem(dragitem);
        dataviewright.refreshItem(dragitem);
        refreshComponents();

        // ## ANIIMATION
        /*gridsales.getElement().executeJs(
                "const row = this.shadowRoot.querySelector('[part~=\"row\"][index=\"$0\"]');" +
                        "if (row) {" +
                        "    row.classList.add('animated-row');" +
                        "    setTimeout(() => row.classList.remove('animated-row'), 2000);" +
                        "}", dataviewright.getItemIndex(dragitem).get()        );*/

        dragitem = null;
    }

    private void removeService(WorkAdded work) {
        dragitem = work;
        if (work.getCant() > 1) {
            dragitem.setCant(dragitem.getCant() - 1);
            dataviewright.refreshItem(dragitem);
            dataviewleft.refreshItem(dragitem);
            refreshComponents();
        } else {
            deleteServiceGrid();
        }
    }

    private void deleteServiceGrid() {
//        dragitem.setAdded(false);
        dragitem.setCant(0);
        dataviewleft.refreshItem(dragitem);
        dataviewright.removeItem(dragitem);
        dragitem = null;
        refreshComponents();
    }

    private void refreshComponents() {

        //Refresca el valor a pagar
        total.setText(formatNumber(getTotalWorked()));

        //Refresca estados de los botones
        btnContinuar.setEnabled(gridsales.getListDataView().getItemCount() > 0);
        btnLimpiar.setEnabled(gridsales.getListDataView().getItemCount() > 0);
    }

    /**
     * Limpia la zona de Trabajo
     */
    public void resetWorkspace() {
        gridservices.setItems(new ArrayList<>());
        gridsales.setItems(new ArrayList<>());
        loadDataGridServices();
    }

    @NotNull
    private Div getIconItem(Product product, Div itemservice) {
        Div div = new Div();
        div.addClassName("item-service-avatar");
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            div.add(new SvgIcon("line-awesome/svg/" + product.getImage() + ".svg"));
        } else {
            Avatar avatar = new Avatar();
            avatar.addClassName("avatar-service");
            avatar.setName(product.getTitle());
            div.addClassName("avatar");
            div.add(avatar);
        }
        itemservice.add(div);
        Div div2 = new Div();
        H5 h5 = new H5(product.getTitle());
        Span price = new Span(formatNumber(product.getPrice()));
        price.getElement().getThemeList().add("badge contrast");
        div2.add(h5, price);
        itemservice.add(div2);
        return itemservice;
    }


    private Icon markIcon() {
        Icon icon = VaadinIcon.CHECK.create();
        icon.addClassName("check-service");
        icon.getElement().getThemeList().add("badge success");
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        // Accessible label
        icon.getElement().setAttribute("aria-label", "Agregado");
        // Tooltip
        icon.getElement().setAttribute("title", "Agregado");
        return icon;
    }

    //Mini Formulario

    /**
     * Formulario
     *
     * @param work
     * @return
     */
    private Component createForm(WorkAdded work) {
        Div div = new Div();
        div.addClassName("mini-form");
        Button minus = new Button();
        minus.addThemeVariants(ButtonVariant.LUMO_SMALL);
        minus.addClassNames(LumoUtility.Padding.XSMALL, LumoUtility.Margin.XSMALL);
        if (work.getCant() > 1) {
            minus.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
            minus.setIcon(VaadinIcon.MINUS.create());
        } else {
            minus.addThemeVariants(ButtonVariant.LUMO_ERROR);
            minus.setIcon(VaadinIcon.TRASH.create());
        }
        minus.addClickListener(event -> removeService(work));
        Button plus = new Button(VaadinIcon.PLUS.create());
        plus.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_CONTRAST);
        plus.addClassNames(LumoUtility.Padding.XSMALL, LumoUtility.Margin.XSMALL);
        plus.addClickListener(event -> addServiceGrid(work));

        NumberField cant = new NumberField();
        cant.addValueChangeListener(event -> {
            if (cant == null) {
                return;
            }
            cant.getElement().getClassList().add("animated-numberfield");

            // Usar un temporizador para eliminar la clase después de la animación
            cant.getElement().executeJs(
                    "setTimeout(() => $0.classList.remove('animated-numberfield'), 500);", cant.getElement()
            );
        });
        cant.addClassName("number-service");
        cant.setStep(1);
        cant.setReadOnly(true);
        cant.setValue((double) work.cant);
        div.add(minus, cant, plus);
        return div;
    }

    private String formatNumber(Double number) {
        return new DecimalFormat("#,###.##").format(number);
    }


    //## DATA
    public Double getTotalWorked() {
        return gridsales.getListDataView()
                .getItems()
                .toList()
                .stream()
                .mapToDouble(item -> item.subTotal())
                .sum();
    }

    /**
     * Consulta las estadisticas del perfil
     */
    private HorizontalLayout getProgreso() {
        HorizontalLayout p = new HorizontalLayout();
        p.addClassName("toolbar-progress");
        ProgressBar progressBar = new ProgressBar();
        progressBar.setValue(0.8);
        p.add(progressBar);
        return p;
    }


    /**
     * Oculta o muestra los elementos de la UI
     *
     * @param hide | True para ocultar
     */
    private void controlShowViews(Boolean hide) {
        if (hide) {
            divfinal.addClassName("finish-on");
            divleft.addClassName("no-visible");
            divright.addClassName("no-visible");
            divsummary.addClassName("no-visible");
            divtoolbar.addClassName("no-visible");
        } else {
            divfinal.removeClassName("finish-on");
            divtoolbar.removeClassName("no-visible");
            divleft.removeClassName("no-visible");
            divright.removeClassName("no-visible");
            divsummary.removeClassName("no-visible");
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (Application.getModeApp() == 0) {
            event.forwardTo(WelcomeModeView.class);
        } else if (Application.getModeApp() == 1 && Application.getProfile() == null) {
            event.forwardTo(SelectProfileView.class);
        } else if (Application.getModeApp() == 2 && Application.getProfile() == null) {
            event.forwardTo(WelcomeProfileView.class);
        }
//        UI.getCurrent().getPage().executeJs(
//                "window.addEventListener('beforeunload', function (event) {" +
//                        "  event.preventDefault();" +
//                        "  event.returnValue = 'Tienes cambios sin guardar. ¿Estás seguro de que quieres salir?';" +
//                        "});"
//        );

        if (modeApp == 2) {
            UI.getCurrent().getPage().setTitle("Trabajando: MODO PERFIL");
        } else {
            UI.getCurrent().getPage().setTitle("Trabajando: MODO ASISTIDO");
        }
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        if (gridsales.getListDataView().getItemCount() > 0) {
            // Evitar la navegación y mostrar el diálogo de confirmación
            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader("Cambios sin guardar");
            dialog.setText("Tienes cambios sin guardar. ¿Estás seguro de que quieres salir de esta página?");
            dialog.setCancelable(true);
            dialog.setConfirmText("Salir");
            dialog.setCancelText("Cancelar");
            dialog.setCancelButtonTheme(ButtonVariant.LUMO_CONTRAST.getVariantName());
            dialog.setConfirmButtonTheme(ButtonVariant.LUMO_ERROR.getVariantName());

            dialog.addConfirmListener(e -> {
                // Si el usuario confirma, permite la navegación
                // Se asume que si el usuario confirmó, está bien salir
                event.getContinueNavigationAction().proceed();  // Permitir la navegación
            });

            dialog.open();

            // Cancelar la navegación temporalmente hasta que el usuario confirme
            event.postpone();
        }
    }


    //Class
    public class WorkAdded {
        private Product servicio;
        private int cant;
        private boolean added;

        public Product getServicio() {
            return servicio;
        }

        public void setServicio(Product servicio) {
            this.servicio = servicio;
        }

        public int getCant() {
            return cant;
        }

        public void setCant(int cant) {
            this.cant = cant;
        }

        public Double subTotal() {
            return cant * getServicio().getPrice();
        }

        public boolean isAdded() {
            return added;
        }

        public void setAdded(boolean added) {
            this.added = added;
        }
    }

}
