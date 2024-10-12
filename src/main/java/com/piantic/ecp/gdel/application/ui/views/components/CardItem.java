package com.piantic.ecp.gdel.application.ui.views.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("card-item")
public class CardItem extends Div {

    public CardItem(){
    }

    public CardItem(SvgIcon iconInfo, String label_, Class navigateClass) {
        addClassName("actionquick-item");
        addClassNames(LumoUtility.Gap.MEDIUM, LumoUtility.Display.FLEX
                , LumoUtility.FlexDirection.ROW, LumoUtility.Background.BASE
                , LumoUtility.AlignItems.CENTER, LumoUtility.TextColor.PRIMARY
                , LumoUtility.BorderRadius.MEDIUM, LumoUtility.Padding.MEDIUM
                , LumoUtility.BoxShadow.XSMALL);
        getStyle().set("cursor", "pointer");
        Div divIcon = new Div();
        divIcon.addClassName("actionquick-icon-item");

        divIcon.add(iconInfo);

        H5 label = new H5(label_);
        label.addClassNames(LumoUtility.Margin.NONE, LumoUtility.TextColor.PRIMARY, LumoUtility.FontWeight.LIGHT);

        addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate(navigateClass));
        });
        add(divIcon, new Div(label));
    }

}
