package com.piantic.ecp.gdel.application.backend.utils;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MessagesUtil {


    public static Div showWarning(String message){
        Div div = createDefaultDiv(message);
        div.addClassNames(
                LumoUtility.Background.WARNING_10,
                LumoUtility.TextColor.WARNING
        );
        return div;
    }

    public static Div showSucces(String message){
        Div div = createDefaultDiv(message);
        div.addClassNames(
                LumoUtility.Background.SUCCESS_10,
                LumoUtility.TextColor.SUCCESS
        );
        return div;
    }

    public static Div showPrimary(String message){
        Div div = createDefaultDiv(message);
        div.addClassNames(
                LumoUtility.Background.PRIMARY_10,
                LumoUtility.TextColor.PRIMARY
        );
        return div;
    }

    public static Div showTertiary(String message){
        Div div = createDefaultDiv(message);
        div.addClassNames(
                LumoUtility.Background.CONTRAST_10,
                LumoUtility.TextColor.TERTIARY
        );
        return div;
    }

    private static Div createDefaultDiv(String message) {
        Div div = new Div(message);
        div.addClassNames(
                LumoUtility.FontSize.SMALL,
                LumoUtility.Padding.SMALL,
                LumoUtility.BorderRadius.MEDIUM
        );
        return div;
    }
}
