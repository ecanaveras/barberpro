package com.piantic.ecp.gdel.application.backend.analytic;

import java.util.ArrayList;

public class AnalyticSeries {

    private String name;

    private ArrayList data = new ArrayList();


    public void addSerieDouble(String name, ArrayList<Double> valuesdouble) {
        this.name=name;
        this.data = valuesdouble;
    }

    public void addSerieNumber(String name, ArrayList<Number> valuesnumber) {
        this.name=name;
        this.data = valuesnumber;
    }

    public void addSerieInteger(String name, ArrayList<Integer> valuesinteger) {
        this.name=name;
        this.data = valuesinteger;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList getData() {
        return data;
    }

    public void setData(ArrayList data) {
        this.data = data;
    }
}
