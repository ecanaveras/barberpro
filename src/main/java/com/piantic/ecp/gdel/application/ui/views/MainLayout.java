package com.piantic.ecp.gdel.application.ui.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

/**
 * The main view is a top-level placeholder for other views.
 */
@Route("")
public class MainLayout extends VerticalLayout implements AfterNavigationObserver {


    public MainLayout() {
        // getUI().ifPresent( ui -> ui.navigate(CustomerView.class));
    }


    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        getUI().ifPresent( ui -> ui.navigate(CustomerView.class));
    }
}
