package com.piantic.ecp.gdel.application.ui.views.specials;

import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("error")
public class ErrorView extends VerticalLayout {

    public ErrorView() {
        setSizeFull();
        setSpacing(true);
        H5 errorMesssage = new H5("Ha ocurrido un error!");
        add(errorMesssage);
    }

}
