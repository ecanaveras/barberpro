package com.piantic.ecp.gdel.application.ui.views.forms;

import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.entity.Role;
import com.piantic.ecp.gdel.application.backend.service.ProductService;
import com.piantic.ecp.gdel.application.backend.service.ProfileService;
import com.piantic.ecp.gdel.application.backend.utils.NotificationUtil;
import com.piantic.ecp.gdel.application.ui.views.specials.GenericForm;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.vaadin.lineawesome.LineAwesomeIcon;


public class RoleForm extends GenericForm<Role> {

    private ProductService productService;
    private ProfileService profileService;
    private Role updateitem;

    TextField name = new TextField("Nombre del Role");

    MultiSelectListBox<Profile> mlboxprofiles = new MultiSelectListBox<>();

    public RoleForm(ProductService productService, ProfileService profileService) {
        super();
        addClassName("role-form");
        this.productService = productService;
        this.profileService = profileService;

        //Binder
        binder.bindInstanceFields(this);
//        binder.withValidator(role -> mlboxservices.getSelectedItems().size() > 0, "Debe agregar al menos un permiso antes de guardar");

        configureGrids();

        //Textfields
        name.setPrefixComponent(LineAwesomeIcon.OBJECT_GROUP.create());
        name.setAutofocus(true);

        //Form
//        formLayout.add(name, avalaibles, attacths);

        VerticalLayout vlprofiles = new VerticalLayout();
        vlprofiles.add(new Span(new Span(LineAwesomeIcon.USER_CIRCLE.create()), new Span("Vincular Perfiles")), mlboxprofiles);

        formLayout.add(name);
        formLayout.add(vlprofiles);
        formLayout.setColspan(name, 2);
        formLayout.setColspan(vlprofiles, 2);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));

        if (updateitem != null) {
            binder.readBean(updateitem);
        }

        btnSave.addClickListener(event -> {
            if (mlboxprofiles.getSelectedItems().isEmpty()) {
                NotificationUtil.showWarning("El ROLE no tiene Perfiles, asignados");
            }

        });
    }


    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        updateitem = this.getEntity();
        setHeaderTitle(getEntity().getName() != null ? "Editando Role" : "Nuevo Role");
        binder.readBean(updateitem);
        profileService.getProfileByRoleId(updateitem.getId()).forEach(profile -> mlboxprofiles.select(profile));
    }


    private void configureGrids() {
        //Perfiles
        mlboxprofiles.setItems(profileService.findAll());
        mlboxprofiles.setItemLabelGenerator(Profile::getNameProfile);
        mlboxprofiles.addSelectionListener(listener -> {
            profileSet = mlboxprofiles.getSelectedItems();
        });
    }


}
