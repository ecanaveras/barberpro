package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.backend.analytic.AnalyticSeries;
import com.piantic.ecp.gdel.application.backend.entity.Appointment;
import com.piantic.ecp.gdel.application.backend.service.AppointmentService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datepicker.DatePickerVariant;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@PageTitle("Dashboard | Barberpro")
@JsModule("https://code.highcharts.com/highcharts.js")
@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends Div {

    private Div divcolumnchart;
    private Div divlinechart;
    private DatePicker dateStart;
    private DatePicker dateEnd;
    private AppointmentService appointmentService;

    public DashboardView(AppointmentService appointmentService) {
        addClassName("dashboard-view");
        this.appointmentService = appointmentService;

        add(createToolbar());

        divlinechart = new Div();
        divlinechart.setId("container-line-chart");

        divcolumnchart = new Div();
        divcolumnchart.setId("container-column-chart");

        add(divlinechart);
        add(divcolumnchart);
    }

    private Component createToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidthFull();
        toolbar.addClassName("dash-toolbar");

        dateStart = new DatePicker("Desde");
        dateEnd = new DatePicker("Hasta");
        dateStart.addThemeVariants(DatePickerVariant.LUMO_SMALL);
        dateEnd.addThemeVariants(DatePickerVariant.LUMO_SMALL);
        dateStart.setValue(LocalDate.now());
        dateEnd.setValue(LocalDate.now());
        dateStart.addValueChangeListener(event -> dateEnd.setMin(event.getValue()));
        dateEnd.addValueChangeListener(event -> dateStart.setMax(event.getValue()));

        Button btnFilter = new Button("Filtrar");
        btnFilter.addThemeVariants(ButtonVariant.LUMO_SMALL);
        btnFilter.addClickListener(listener -> {
            loadInfoAppointmentsLineChart();
            loadInfoAppointmentsColumnChart();
        });

        toolbar.add(dateStart, dateEnd, btnFilter);

        return toolbar;
    }

    private void loadInfoAppointmentsLineChart() {
//        ArrayList<String> categorias = new ArrayList<>(Arrays.asList("'Jan'", "'Feb'", "'Mar'", "'Apr'", "'May'", "'Jun'", "'Jul'", "'Aug'", "'Sep'", "'Oct'", "'Nov'", "'Dec'"));
        ArrayList<String> categorias = new ArrayList<>();
        ArrayList<AnalyticSeries> series = new ArrayList<>();
//        ArrayList<Double> valores = new ArrayList<>(Arrays.asList(5.1,3.2,8.9,0.0,10.1,25.3,80.0,3.0,45.0,56.0,11.0,12.0));

        Map<String, Map<String, Double>> groupedAppointments = appointmentService.findByDateRange(dateStart.getValue(), dateEnd.getValue())
                .stream()
                .collect(Collectors.groupingBy(
                        appointment -> appointment.getProfile().getNameProfile(),
                        Collectors.collectingAndThen(
                                Collectors.groupingBy(
                                        appointment -> appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                        Collectors.summingDouble(Appointment::getTotal)
                                ),
                                TreeMap::new
                        )
                ));

        groupedAppointments.forEach((profile, dateMap) -> {
            System.out.println("Profile: " + profile);
            AnalyticSeries newserie = new AnalyticSeries();
            ArrayList<Double> valores = new ArrayList<>();
            dateMap.forEach((date, total) -> {
                System.out.println("Category: " + date + " Value: " + total);
                categorias.add(encapsuleString(date.toString()));
                valores.add(total);
            });
            newserie.addSerieDouble(profile, valores);
            series.add(newserie);
        });


        getElement().executeJs(getLineChart(divlinechart, categorias, series));
    }

    private void loadInfoAppointmentsColumnChart() {
        ArrayList<String> categorias = new ArrayList<>();
        ArrayList<AnalyticSeries> series = new ArrayList<>();

        List<Object[]> resulquery = appointmentService.findAggProductProfileDate(dateStart.getValue(), dateEnd.getValue());
        AnalyticSeries newserie = new AnalyticSeries();
        ArrayList<Number> valores = new ArrayList<>();
        for (Object[] row : resulquery) {
            //ProductName
            categorias.add(encapsuleString((String) row[1]));

            //perfil
            String profileName = (String) row[0];
            if (newserie.getName() == null) {
                newserie.setName(profileName);
                series.add(newserie);
            }
            if (newserie.getName().equals(profileName)) {
                //Subtotal
                valores.add((Number) row[2]);
                newserie.setData(valores);
            } else {
                valores = new ArrayList<>();
                //Subtotal
                valores.add((Number) row[3]);
                newserie = new AnalyticSeries();
                newserie.addSerieNumber(profileName, valores);
                series.add(newserie);
            }

        }

        getElement().executeJs(getColumnChart(divcolumnchart, categorias, series));
        System.out.println(getColumnChart(divcolumnchart, categorias, series));
    }

    /**
     * Formatea las series para Hihgcharts
     *
     * @param series
     * @return
     */
    private String formatSeries(ArrayList<AnalyticSeries> series) {
        String seriesfinal = " ";
        for (AnalyticSeries serie : series) {
            seriesfinal = seriesfinal + "  { name: '" + serie.getName() + "', data: " + serie.getData() + " } ,";
        }
        //Quita la coma final
        seriesfinal = seriesfinal.substring(0, seriesfinal.length() - 1);
        return seriesfinal;
    }

    private String getLineChart(Div divrender, ArrayList<String> categorias, ArrayList<AnalyticSeries> series) {

        return "Highcharts.chart('" + divrender.getId().get() + "', {" +
                "    chart: {" +
                "        type: 'line'" +
                "    }," +
                "    title: {" +
                "        text: 'Ingresos por Perfil'," +
                "        align: 'left'" +
                "    }," +
                "    xAxis: {" +
                "        categories: " + categorias +
                "    }," +
                "    yAxis: {" +
                "        title: {" +
                "            text: ''" +
                "        }" +
                "    }," +
//                "    series: [{" +
//                "        name: 'Sample Data'," +
//                "        data: " + valores +
//                "    }]" +
                " series: [" + formatSeries(series) + " ]" +
                " });";
    }

    private String getColumnChart(Div divrender, ArrayList<String> categorias, ArrayList<AnalyticSeries> series) {
        return "Highcharts.chart('" + divrender.getId().get() + "', {" +
                "    chart: {" +
                "        type: 'column'" +
                "    }," +
                "    title: {" +
                "        text: 'Acumulado de Servicios por Perfil'," +
                "        align: 'left'" +
                "    }," +
                "    subtitle: {" +
                "        text:" +
                "            'Source: <a target=\"_blank\" ' +" +
                "            'href=\"https://www.indexmundi.com/agriculture/?commodity=corn\">indexmundi</a>'," +
                "        align: 'left'" +
                "    }," +
                "    xAxis: {" +
                "        categories: " + categorias + "," +
                "        crosshair: true," +
                "        accessibility: {" +
                "            description: 'Servicios | Productos'" +
                "        }" +
                "    }," +
                "    yAxis: {" +
                "        min: 0," +
                "        title: {" +
                "            text: '$ Acumulado'" +
                "        }" +
                "    }," +
                "    tooltip: {" +
//                "        valueSuffix: ' (1000 MT)'" +
                "    }," +
                "    plotOptions: {" +
                "        column: {" +
                "            pointPadding: 0.2," +
                "            borderWidth: 0" +
                "        }" +
                "    }," +
                " series: [" + formatSeries(series) + " ]" +
                "});";
    }

    private String encapsuleString(String string) {
        string = "'" + string + "'";
        return string;
    }

    private ArrayList<String> transformDataString(ArrayList<Object> data) {
        ArrayList<String> dataout = new ArrayList<>();
        data.forEach(obj -> dataout.add("'" + obj.toString() + "'"));
        return dataout;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        loadInfoAppointmentsLineChart();
        loadInfoAppointmentsColumnChart();
    }


}
