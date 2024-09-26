package com.piantic.ecp.gdel.application.backend.utils;

import java.text.DecimalFormat;

public class NumberUtil {

    public static String formatDouble(Number number) {
        return new DecimalFormat("#,###.##").format(number);
    }

    public static String formatInteger(double number) {
        return new DecimalFormat("#,###").format(number);
    }

    public static String formatNumber(Number number) {
        if(number instanceof Double){
            return new DecimalFormat("#,###.##").format(number);
        }
        //Integer format
        return new DecimalFormat("#,###").format(number);
    }
}
