package com.piantic.ecp.gdel.application.ui.views.specials;

import com.piantic.ecp.gdel.application.Application;
import com.piantic.ecp.gdel.application.backend.entity.Product;
import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.entity.ProfileProduct;
import com.piantic.ecp.gdel.application.backend.utils.generics.CloseEventListener;
import com.piantic.ecp.gdel.application.backend.utils.generics.SaveEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.HashSet;
import java.util.Set;

public class GenericForm<T> extends Dialog {

    public FormLayout formLayout = new FormLayout();

    public Button btnSave = new Button("Guardar");
    Button btnCancel = new Button("Cancelar");

    private T entity;
    public Set<Profile> profileSet = new HashSet<>();

    public Binder<T> binder = new BeanValidationBinder<>(getGenericType());

    private SaveEventListener<T> saveEventListener;
    private CloseEventListener closeEventListener;

    public GenericForm() {
        addClassName("generic-form");
//        binder.bindInstanceFields(this);

        btnSave.setEnabled(false);

        // Configure TextFields


        setButtons();
        HorizontalLayout btns = new HorizontalLayout(btnSave, btnCancel);
        btns.setWidthFull();

        //setHeaderTitle("New Entry");
        Button btnCloseDialog = new Button(LineAwesomeIcon.TIMES_CIRCLE.create(), e -> this.close());
        btnCloseDialog.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);
        getHeader().add(btnCloseDialog);
        add(formLayout);
        getFooter().add(btns);
    }

    private void setButtons() {
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnCancel.addClickShortcut(Key.ESCAPE);

        btnCancel.addClickListener(e -> closeDialog());
        btnSave.addClickListener(e -> validateAndSave());
        btnSave.addClickShortcut(Key.ENTER);

        binder.addStatusChangeListener(e -> btnSave.setEnabled(binder.isValid()));
    }

    private void validateAndSave() {
        if (binder.isValid() && saveEventListener != null) {
            if(binder.getBean() instanceof Product){
                Set<ProfileProduct> profileProduct = new HashSet<>();
                profileSet.forEach(profile -> {
                    profileProduct.add(new ProfileProduct(profile, (Product) binder.getBean(), Application.getTenant()));
                });
                ((Product) binder.getBean()).setProfilesProducts(profileProduct);
            }
            saveEventListener.onSave(binder.getBean());
            this.close();
        } else {
            System.out.println("Binder not valid");
        }
    }

    private void closeDialog() {
        if (closeEventListener != null) {
            closeEventListener.onClose();
        }
        this.close();
    }

    public void setEntity(T entity) {
        binder.setBean(entity);
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }

    // This method helps to determine the generic type at runtime
    @SuppressWarnings("unchecked")
    private Class<T> getGenericType() {
        return (Class<T>) ((java.lang.reflect.ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void setSaveEventListener(SaveEventListener<T> saveEventListener) {
        this.saveEventListener = saveEventListener;
    }

    public void setCloseEventListener(CloseEventListener closeEventListener) {
        this.closeEventListener = closeEventListener;
    }
}
