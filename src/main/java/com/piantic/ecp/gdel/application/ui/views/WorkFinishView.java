package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.backend.utils.generics.CloseEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;

import java.util.stream.Stream;

public class WorkFinishView extends VerticalLayout {

    Stream<WorkingView.WorkAdded> list;
    private CloseEventListener closeEventListener;

    public WorkFinishView(Stream<WorkingView.WorkAdded> workAddeds) {
        addClassName("work-finish-view");
        this.list = workAddeds;
        setPadding(false);

        H2 title = new H2("Work Finish");

        H3 cliente = new H3("Cliente");
        Span name = new Span("NombreCliente");

        H3 servicios = new H3("Servicios");

        Span span = new Span();
        span.getElement().getThemeList().add("badge success");
        span.setText(String.valueOf(list.count()));


        H3 totaltoPay = new H3("Total");
        NumberField numbertoPay = new NumberField("$");
        numbertoPay.setEnabled(false);
        numbertoPay.setValue(sutTotal());

        VerticalLayout layout = new VerticalLayout(
                title
                , cliente
                , name
                , new HorizontalLayout(servicios, span)
                , new HorizontalLayout(totaltoPay, numbertoPay)
        );

        layout.setPadding(false);
        layout.setMargin(false);

        add(layout);

        Button btnSave = new Button("Guardar & Continuar");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.setSizeFull();
        btnSave.addClickListener(e -> {
            Notification notification = new Notification();
            notification.add(new HorizontalLayout(VaadinIcon.CHECK_CIRCLE.create(), new Text("Servicio Guardado")));
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(3000);
            notification.open();
            this.closeView();
        });


        Button btnCancel = new Button("Cancelar");
        btnCancel.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnCancel.setSizeFull();
        btnCancel.addClickListener(e -> {
            this.closeView();
        });

        add(new VerticalLayout(btnSave, btnCancel));
    }

    public Double sutTotal() {
//        Double val = list.mapToDouble(item -> item.subTotal()).sum();
        return 0.0;
    }

    public void closeView() {
        this.setVisible(false);
        if (closeEventListener != null) {
            closeEventListener.onClose();
        }
    }

    public void setCloseEventListener(CloseEventListener closeEventListener) {
        this.closeEventListener = closeEventListener;
    }
}
