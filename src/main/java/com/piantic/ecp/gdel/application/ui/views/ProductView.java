package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.Application;
import com.piantic.ecp.gdel.application.backend.entity.Product;
import com.piantic.ecp.gdel.application.backend.service.ProfileService;
import com.piantic.ecp.gdel.application.backend.service.ProductService;
import com.piantic.ecp.gdel.application.backend.utils.NotificationUtil;
import com.piantic.ecp.gdel.application.ui.views.forms.ProductForm;
import com.vaadin.flow.component.avatar.Avatar;
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

import java.text.DecimalFormat;

@PageTitle("Servicios | Productos")
@Route(value = "product", layout = MainLayout.class)
public class ProductView extends HorizontalLayout implements HasUrlParameter<Long> {

    private final TextField txtFilter;
    private final Button btnAdd;
    private final Span count = new Span();
    private final ProfileService profileService;
    private Div contentRight;
    private VerticalLayout contentLeft;
    //private WordS customerviewdetail;
    private Grid<Product> grid = new Grid<>(Product.class, false);
    public ProductService productService;
    private Boolean detailAdded = false;

    public ProductView(ProductService productService, ProfileService profileService) {
        addClassName("work-view");
        setSizeFull();


        this.productService = productService;
        this.profileService = profileService;

        //Toolbar
        txtFilter = new TextField();
        txtFilter.setClearButtonVisible(true);
        txtFilter.setPlaceholder("Filtrar...");
        txtFilter.setValueChangeMode(ValueChangeMode.LAZY);
        txtFilter.addValueChangeListener(e -> updateList());
        txtFilter.addClassNames(LumoUtility.Flex.AUTO);
        txtFilter.setMaxWidth("26rem");

        btnAdd = new Button(LineAwesomeIcon.MAGIC_SOLID.create());
        btnAdd.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnAdd.setTooltipText("Agregar...");
        btnAdd.addClickListener(click -> {
            openFormDialog(new Product());
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

    private void openFormDialog(Product product) {
        ProductForm productForm = new ProductForm(profileService);
        productForm.setEntity(product);
        productForm.setSaveEventListener(e -> {
            this.saveWork(e);
            productForm.close();
        });
        productForm.open();
    }

    private void configureGrid() {
        grid.addClassName("work-grid");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addComponentColumn(work -> {
            HorizontalLayout content = new HorizontalLayout();
            content.setAlignItems(Alignment.CENTER);
            VerticalLayout layout = new VerticalLayout();
            layout.addClassName("work-grid-service");
            layout.add(new H5(work.getTitle()),new Span(work.getDescription()));
            content.add(new Avatar(work.getTitle()), layout);
           return content;
        }).setAutoWidth(true).setHeader("Servicio").setSortable(true).setComparator(Product::getTitle).getStyle().set("min-width", "200px");

        grid.addComponentColumn(product -> {
            Span span = new Span();
            span.setText(new DecimalFormat("$ #,###.##").format(product.getPrice()));
            span.getElement().getThemeList().add("badge success");
            return span;
        }).setHeader("Precio").setComparator(Product::getPrice);

        grid.addComponentColumn(product -> {
            Span span = new Span();
            span.setText(new DecimalFormat("###.##").format(product.getCommissions())+" %");
            span.getElement().getThemeList().add("badge");
            return span;
        }).setHeader("Comisión").setComparator(Product::getCommissions);

        grid.addComponentColumn(product -> {
            Span span = new Span();
            span.getElement().getThemeList().add("badge warning");
//            product.getProfiles().forEach(profileProduct -> {
//                span.setText(profileProduct.getProfile().getNameProfile());
//            });
            profileService.findProfileByProduct(product).forEach(profile -> {
                span.setText(profile.getNameProfile());
            });
            return span;
        }).setHeader("Perfiles");
        grid.addComponentColumn(product -> {
            Button btnedit = new Button("Editar", LineAwesomeIcon.EDIT.create());
            btnedit.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_ICON);
            btnedit.addClickListener(event -> {
                openFormDialog(product);
            });
            return btnedit;
        }).setHeader("");

//        grid.addColumn("observations").setHeader("Observaciones");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));



        //grid.addColumns("price", "commissions", "description", "observations");
//        grid.addColumn(Customer::getPhone).setHeader("Teléfono").setSortable(true);
//        grid.addColumn(Customer::getEmail).setHeader("Email").setSortable(true);
//        grid.addComponentColumn(customer -> {
//            SvgIcon star = LineAwesomeIcon.STAR_SOLID.create();
//            star.addClassName("star-icon");
//            return customer != null && customer.isFavorite() ? star : null;
//        }).setHeader("Favorito").setWidth("6rem");

        createMenu();

        grid.asSingleSelect().addValueChangeListener(e -> showDetail(e.getValue() != null ? e.getValue().getId() : null));
    }

    private void updateList() {
        grid.setItems(productService.findAll(Application.getTenant(), txtFilter.getValue()));
        count.setText(String.valueOf(productService.count()));
    }

    public void saveWork(Product product) {
        productService.save(product);
        updateList();
        NotificationUtil.showSuccess("Servicio Guardado!");
    }

    private void deleteWork(Product product) {
        ConfirmDialog confirmDialog = new ConfirmDialog("¿Eliminar a \"" + product.getTitle() + "\"?",
                "¿Desea borrar el registro?",
                "Eliminar", e -> {
            productService.delete(product);
            updateList();
            getUI().ifPresent(ui -> ui.navigate(ProductView.class));
        }, "Cancelar", e -> e.getSource().close());
        confirmDialog.setConfirmButtonTheme(ButtonVariant.LUMO_ERROR.getVariantName() + " " + ButtonVariant.LUMO_PRIMARY.getVariantName());
        confirmDialog.setCloseOnEsc(true);
        confirmDialog.open();

    }

    private void createMenu() {
        GridContextMenu<Product> menu = grid.addContextMenu();
        Span new1 = new Span("Nuevo");
        new1.setClassName(LumoUtility.TextColor.PRIMARY);
        menu.addItem(new HorizontalLayout(new1, LineAwesomeIcon.MAGIC_SOLID.create()), e -> openFormDialog(new Product()));
        menu.addItem("Editar", e -> openFormDialog(e.getItem().get()));
        menu.addItem("Detalles", e -> showDetail(e.getItem().get().getId()));
        menu.add(new Hr());
        GridMenuItem itemDel = menu.addItem("Eliminar", e -> deleteWork(e.getItem().get()));
        itemDel.addClassNames(LumoUtility.TextColor.ERROR);
    }

    private void showDetail(Long id) {
        if (id != null) {
            getUI().ifPresent(ui -> ui.navigate(ProductView.class, id));
        } else {
            getUI().ifPresent(ui -> ui.navigate(ProductView.class));
        }
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long id) {
      /*  if (id != null) {

            if (workService.findByCustomerId(id) == null) {
//                beforeEvent.rerouteToError(IllegalArgumentException.class, getTranslation("customer.not.found", beforeEvent.getLocation().getPath()));
//                beforeEvent.forwardTo("error");
                Notification noti = new Notification();
                noti.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
                Div text = new Div(new Text("Registro no encontrado..."));
                Button closeBtn = new Button(new Icon("lumo", "cross"));
                closeBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
                closeBtn.setAriaLabel("Cerrar");
                closeBtn.addClickListener(e -> {
                    noti.close();
                });
                HorizontalLayout layout = new HorizontalLayout(text, closeBtn);
                layout.setAlignSelf(Alignment.CENTER);

                noti.add(layout);
                noti.setPosition(Notification.Position.MIDDLE);
                noti.open();
                return;
            }
            if (customerviewdetail != null && customerviewdetail.getCustomer() != null && customerviewdetail.getCustomer().getId().equals(id)) {
                if(contentRight.getClassNames().contains("visible")) {
                    contentLeft.removeClassName("viewing");
                    contentRight.removeClassName("visible");
                    return;
                }
            }
            if (customerviewdetail == null) {
                customerviewdetail = new CustomerViewDetail(workService, id);
                contentRight.add(customerviewdetail);
            } else {
                customerviewdetail.updateUI(id);
            }
            contentRight.addClassName("visible");
            contentLeft.addClassName("viewing");


        } else {
            if (customerviewdetail != null) {
                contentLeft.removeClassName("viewing");
                contentRight.removeClassName("visible");
            }
        }*/
    }

}
