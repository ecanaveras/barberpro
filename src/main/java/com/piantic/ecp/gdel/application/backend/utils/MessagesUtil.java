package com.piantic.ecp.gdel.application.backend.utils;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MessagesUtil {

    public static Div showWarning(String message){
        Div div = new Div(message);
        div.addClassNames(
                LumoUtility.FontSize.SMALL,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.Background.WARNING_10,
                LumoUtility.TextColor.WARNING,
                LumoUtility.BorderRadius.SMALL
        );
        return div;
    }

    public static Div showSucces(String message){
        Div div = new Div(message);
        div.addClassNames(
                LumoUtility.FontSize.SMALL,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.Background.SUCCESS_10,
                LumoUtility.TextColor.SUCCESS,
                LumoUtility.BorderRadius.SMALL
        );
        return div;
    }

    public static Div showPrimary(String message){
        Div div = new Div(message);
        div.addClassNames(
                LumoUtility.FontSize.SMALL,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.Background.PRIMARY_10,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.BorderRadius.SMALL
        );
        return div;
    }
}
