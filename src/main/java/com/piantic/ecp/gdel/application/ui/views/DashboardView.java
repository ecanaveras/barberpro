package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.backend.analytic.AnalyticSeries;
import com.piantic.ecp.gdel.application.backend.service.AppointmentService;
import com.piantic.ecp.gdel.application.backend.utils.MessagesUtil;
import com.piantic.ecp.gdel.application.backend.utils.NumberUtil;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datepicker.DatePickerVariant;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PageTitle("Dashboard | Barberpro")
@JsModule("https://code.highcharts.com/highcharts.js")
@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends Div {

    private Div divoverview;
    private Div divcolumnchart;
    private Div divlinechart;
    private Div divpiechart;
    private Div divpietwochart;
    private DatePicker dateStart;
    private DatePicker dateEnd;
    private AppointmentService appointmentService;

    public DashboardView(AppointmentService appointmentService) {
        addClassName("dashboard-view");
        this.appointmentService = appointmentService;

        add(createToolbar());
        createOverView();
        add(divoverview);

        //TODO Agregar Items Cantidad Servicios, Perfiles, Total Trabajos, Total Acumulado, Total Comision

        divlinechart = new Div();
        divlinechart.addClassName("dashboard-chart");
        divlinechart.setId("container-line-chart");

        divcolumnchart = new Div();
        divcolumnchart.addClassName("dashboard-chart");
        divcolumnchart.setId("container-column-chart");

        divpiechart = new Div();
        divpiechart.addClassName("dashboard-chart");
        divpiechart.setId("container-pie-chart");


        divpietwochart = new Div();
        divpietwochart.addClassName("dashboard-chart");
        divpietwochart.setId("container-pie-two-chart");

        Div divcontent = new Div();
        divcontent.addClassName("dashboard-content");

        divcontent.add(divlinechart);
        divcontent.add(divcolumnchart);
        divcontent.add(divpiechart);
        divcontent.add(divpietwochart);

        add(divcontent);

    }

    private Component createToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidthFull();
        toolbar.addClassName("dash-toolbar");

        dateStart = new DatePicker("Desde");
        dateEnd = new DatePicker("Hasta");
        dateStart.addThemeVariants(DatePickerVariant.LUMO_SMALL);
        dateEnd.addThemeVariants(DatePickerVariant.LUMO_SMALL);
        LocalDate today = LocalDate.now();
        dateStart.setValue(today.minusDays(today.getDayOfMonth() - 1));
        dateEnd.setValue(LocalDate.now());
        dateStart.addValueChangeListener(event -> dateEnd.setMin(event.getValue()));
        dateEnd.addValueChangeListener(event -> dateStart.setMax(event.getValue()));

        Button btnFilter = new Button("Filtrar");
        btnFilter.addThemeVariants(ButtonVariant.LUMO_SMALL);
        btnFilter.addClickListener(listener -> {
            updateLineChart();
            updateColumnChart();
            updatePieChart();
            createOverView();
        });

        Dialog infoFilter = new Dialog("Filtro");
        infoFilter.addClassNames(LumoUtility.MaxWidth.SCREEN_SMALL);
        infoFilter.setCloseOnEsc(true);
        infoFilter.add(new Span("La información de la comparativa es de acuerdo al rango seleccionado."));
        Div contentDialog = new Div();
        contentDialog.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.Padding.SMALL);
        infoFilter.add(contentDialog);
        infoFilter.addAttachListener(a -> {
            contentDialog.removeAll();
            LocalDate startDate = dateStart.getValue();
            LocalDate endDate = dateEnd.getValue();
            startDate = startDate.minusMonths(1);
            endDate = endDate.minusMonths(1);
            DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            contentDialog.add(MessagesUtil.showPrimary("Rango de comparación: " + startDate.format(dateformat) + " - " + endDate.format(dateformat)));
        });
        Button aceptFilter = new Button("Aceptar");
        aceptFilter.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_PRIMARY);
        aceptFilter.addClickListener(event -> infoFilter.close());
        infoFilter.getFooter().add(aceptFilter);

        Button btnOpenDialog = new Button(VaadinIcon.INFO_CIRCLE.create());
        btnOpenDialog.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_CONTRAST);
        btnOpenDialog.addClickListener(e -> infoFilter.open());


        toolbar.add(btnOpenDialog, dateStart, dateEnd, btnFilter);

        return toolbar;
    }

    private Div createOverView() {
        if (divoverview == null) {
            divoverview = new Div();
            divoverview.setId("div-overview");
            divoverview.addClassName("div-overview");
            divoverview.addClassNames(
                    LumoUtility.Display.FLEX,
                    LumoUtility.Gap.MEDIUM,
                    LumoUtility.Padding.MEDIUM);

        }
        //Limpia el div, para asegurar cambios recientes
        divoverview.removeAll();

        //Consulta que contiene la info necesaria
        Double facturado = 0.0;
        Double negocio = 0.0;
        Double comisiones = 0.0;
        Integer trabajos = 0;
        List<Object[]> resulquery = appointmentService.findAggProductProfileDate(dateStart.getValue(), dateEnd.getValue());
        for (Object[] row : resulquery) {
            facturado += ((Number) row[2]).doubleValue();
            negocio += ((Number) row[3]).doubleValue();
            comisiones += ((Number) row[4]).doubleValue();
            trabajos += ((Number) row[5]).intValue();
        }
        //Crecimiento Periodo Anterior
        LocalDate startDate = dateStart.getValue();
        LocalDate endDate = dateEnd.getValue();
        startDate = startDate.minusMonths(1);
        endDate = endDate.minusMonths(1);
        Double incfacturado = 0.0;
        Double incnegocio = 0.0;
        Double inccomisiones = 0.0;
        Integer inctrabajos = 0;
        List<Object[]> resulquery2 = appointmentService.findAggProductProfileDate(startDate, endDate);
        for (Object[] row : resulquery2) {
            incfacturado += ((Number) row[2]).doubleValue();
            incnegocio += ((Number) row[3]).doubleValue();
            inccomisiones += ((Number) row[4]).doubleValue();
            inctrabajos += ((Number) row[5]).intValue();
        }
        divoverview.add(createCardsItems(LineAwesomeIcon.CASH_REGISTER_SOLID.create(),
                "Facturado", facturado, calculateIncrementPorc(incfacturado, facturado), calculateIncrement(incfacturado, facturado)));
        divoverview.add(createCardsItems(LineAwesomeIcon.WALLET_SOLID.create(),
                "Negocio", negocio, calculateIncrementPorc(incnegocio, negocio), calculateIncrement(incnegocio, negocio)));
        divoverview.add(createCardsItems(LineAwesomeIcon.HAND_HOLDING_SOLID.create(),
                "Comisiones", comisiones, calculateIncrementPorc(inccomisiones, comisiones), calculateIncrement(inccomisiones, comisiones)));
        divoverview.add(createCardsItems(LineAwesomeIcon.MAGIC_SOLID.create(),
                "Trabajos", trabajos, calculateIncrementPorc(inctrabajos, trabajos), calculateIncrement(inctrabajos, trabajos).intValue()));
//        divoverview.add(createCardsItems(LineAwesomeIcon.USER.create(), "Cant. Servicios", clientes, 0.0));
        return divoverview;
    }

    private Div createCardsItems(SvgIcon icon, String title, Number val, Number increment, Number diff) {
        Div card = new Div();
        card.addClassName("card-dash-kpi");
        card.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.COLUMN,
                LumoUtility.FlexWrap.WRAP,
                LumoUtility.Gap.XSMALL,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.Background.BASE,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.MEDIUM);

        Span spankpi = new Span(new Span(icon), new Span(title));
        spankpi.addClassName("card-dash-kpi-title");
        card.add(spankpi);

        HorizontalLayout hlayout = new HorizontalLayout();
        hlayout.setPadding(false);

        H4 h4 = new H4(NumberUtil.formatNumber(val));
        Span spanincrement = new Span();
        spanincrement.addClassName("card-dash-kpi-increment");
        spanincrement.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.FontSize.SMALL);
        String format = "%s%s";
        if (increment.intValue() > 0) {
            spanincrement.add(new Span(VaadinIcon.ARROW_UP.create()));
            spanincrement.getElement().getThemeList().add("badge success small pill");
        } else if (increment.intValue() < 0) {
            spanincrement.add(new Span(VaadinIcon.ARROW_DOWN.create()));
            spanincrement.getElement().getThemeList().add("badge error small pill");
        } else {
            spanincrement.getElement().getThemeList().add("badge contrast small pill");
        }
        spanincrement.add(new Span(String.format(format, NumberUtil.formatNumber(increment), "%")));

        //Info periodo anterior
        Span spananterior = new Span();
        spananterior.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.FontSize.XSMALL);
        format = "$%s";
        if(diff instanceof Integer) {
            format = "%s";
        }
        if (diff.intValue() > 0) {
            format = "+" + format;
            spananterior.addClassNames(LumoUtility.TextColor.SUCCESS);
        } else if (diff.intValue() < 0) {
            format = "-" + format;
            spananterior.addClassNames(LumoUtility.TextColor.ERROR);
        } else {
            spananterior.addClassNames(LumoUtility.TextColor.SECONDARY);
        }
        spananterior.setText(String.format(format, NumberUtil.formatNumber(Math.abs(diff.doubleValue()))));
        Span spanlabel = new Span("que el mes anterior");
        spanlabel.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.XXSMALL, LumoUtility.FontWeight.BOLD, LumoUtility.Padding.Left.XSMALL);

        Span spanout = new Span(spananterior, spanlabel);

        card.add(spanincrement);

        hlayout.add(h4, spanincrement);

        card.add(hlayout);

        card.add(spanout);


        return card;

    }

    /**
     * Mapea los datos del Query, para las visualizaciones
     *
     * @param resulquery
     * @param indexCategory
     * @param indexSerie
     * @param indexValue
     * @return
     */
    private Map<ArrayList<String>, ArrayList<AnalyticSeries>> mapDataToChart(List<Object[]> resulquery, Integer indexCategory, Integer indexSerie, Integer indexValue) {

        ArrayList<String> categorias = new ArrayList<>();
        ArrayList<AnalyticSeries> series = new ArrayList<>();

//        List<Object[]> resulquery = appointmentService.findAggWorkProfileDate(dateStart.getValue(), dateEnd.getValue());
        for (Object[] row : resulquery) {
            //(Categorias)
            String fecha = encapsuleString((String) row[indexCategory]);
            if (!categorias.contains(fecha)) {
                categorias.add(fecha);
            }
            //Series
            String profileName = (String) row[indexSerie];
            boolean found = false;
            for (AnalyticSeries s : series) {
                if (s.getName().equals(profileName)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                AnalyticSeries newserie = new AnalyticSeries();
                newserie.setName(profileName);
                series.add(newserie);
            }

        }

        //Mapear valores a las categorias y series
        for (String category : categorias) {
            for (AnalyticSeries serie : series) {
                boolean found = false;
                for (Object[] row : resulquery) {
                    //Cartegoria
                    String fecha = encapsuleString((String) row[indexCategory]);
                    //Series
                    String profileName = (String) row[indexSerie];
                    //Valores | Double
                    Number valor = (Number) row[indexValue];
                    if (category.equals(fecha) && serie.getName().equals(profileName)) {
                        found = true;
                        serie.getData().add(valor);
                        break;
                    }
                }
                if (!found) {
                    serie.getData().add(0);
                }
            }
        }

        Map<ArrayList<String>, ArrayList<AnalyticSeries>> map = new HashMap<>();
        map.put(categorias, series);

        return map;

    }

    private void updateLineChart() {
        List<Object[]> resulquery = appointmentService.findAggWorkProfileDate(dateStart.getValue(), dateEnd.getValue());
        mapDataToChart(resulquery, 1, 0, 2).forEach((category, series) -> {
            getElement().executeJs(getLineChart(divlinechart, category, series));
        });
    }

    private void updateColumnChart() {
        List<Object[]> resulquery = appointmentService.findAggProductProfileDate(dateStart.getValue(), dateEnd.getValue());
        mapDataToChart(resulquery, 1, 0, 2).forEach((category, series) -> {
            getElement().executeJs(getColumnChart(divcolumnchart, category, series));
        });
    }

    private void updatePieChart() {
        List<Object[]> resulquery = appointmentService.findAggProductProfileDate(dateStart.getValue(), dateEnd.getValue());
        List<String> products = new ArrayList<>();
        ArrayList<AnalyticSeries> series = new ArrayList<>();
        for (Object[] row : resulquery) {
            //Serie
            String productName = (String) row[1];
            boolean found = false;
            for (String product : products) {
                if (productName.equals(product)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                products.add(productName);
            }
        }
        //Clone para el segundo pie
        ArrayList<AnalyticSeries> seriesclone = (ArrayList<AnalyticSeries>) series.clone();

        //Recorrer las series para el Agregado
        //Pie Ingresos
        getElement().executeJs(getPieChart(divpiechart, "Ingresos por Servicio", prepareDataPie(products, resulquery, series, 2)));
//        System.out.println(getPieChart(divpiechart, "Ingresos por Servicios", prepareDataPie(products, resulquery, series, 2)));
        //Pie Total Works
        getElement().executeJs(getPieChart(divpietwochart, "Cant. Trabajos por Servicio", prepareDataPie(products, resulquery, seriesclone, 5)));
    }

    private ArrayList<AnalyticSeries> prepareDataPie(List<String> products, List<Object[]> resulquery, ArrayList<AnalyticSeries> series, Integer indexValue) {
        for (String product : products) {
            Double valuequery = 0.0;
            for (Object[] row : resulquery) {
                if (product.equals(row[1].toString())) {
                    //Agg
                    valuequery = valuequery + ((Number) row[indexValue]).doubleValue();
                }
            }
            AnalyticSeries s = new AnalyticSeries();
            s.setName(product);
            ArrayList<Number> valores = new ArrayList<>();
            valores.add(valuequery);
            s.setData(valores);
            series.add(s);
        }

        return series;
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
//                "    series: [{" +
//                "        name: 'Sample Data'," +
//                "        data: " + valores +
//                "    }]" +
            seriesfinal = seriesfinal + "  { name: '" + serie.getName() + "', data: " + serie.getData() + " } ,";
        }
        //Quita la coma final
        seriesfinal = seriesfinal.substring(0, seriesfinal.length() - 1);
        return seriesfinal;
    }

    private String getLineChart(Div divrender, ArrayList<String> categorias, ArrayList<AnalyticSeries> series) {

        return "Highcharts.chart('" + divrender.getId().get() + "', {" +
                "    chart: {" +
                "        type: 'spline'" +
                "    }," +
                "    title: {" +
                "        text: 'Ingresos por Perfil'," +
                "        align: 'left'" +
                "    }," +
                "    xAxis: {" +
                "        categories: " + categorias + "," +
                "        crosshair: true " +
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
                "        text: 'Servicios por Perfil'," +
                "        align: 'left'" +
                "    }," +
//                "    subtitle: {" +
//                "        text:" +
//                "            'Source: <a target=\"_blank\" ' +" +
//                "            'href=\"https://www.indexmundi.com/agriculture/?commodity=corn\">indexmundi</a>'," +
//                "        align: 'left'" +
//                "    }," +
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

    private String getPieChart(Div divrender, String titlePie, ArrayList<AnalyticSeries> series) {
        //Format data for PIE
        String seriesfinal = " ";
        for (AnalyticSeries serie : series) {
//            { name: 'Water', y: 55.02 } ,
            seriesfinal += " { name:'" + serie.getName() + "', y: " + serie.getData().get(0) + " } ,";
        }
        //Quita la coma final
        seriesfinal = seriesfinal.substring(0, seriesfinal.length() - 1);

        return "Highcharts.chart('" + divrender.getId().get() + "', {" +
                "    chart: {" +
                "        type: 'pie'" +
                "    }," +
                "    title: {" +
                "        text: '" + titlePie + "'," +
                "        align: 'left'" +
                "    }," +
//                "    tooltip: {" +
//                "        valueSuffix: '%'" +
//                "    }," +
                "    plotOptions: {" +
                "        series: {" +
                "            allowPointSelect: true," +
                "            cursor: 'pointer'," +
                "            dataLabels: [{" +
                "                enabled: true," +
                "                distance: 10" +
                "            }, {" +
                "                enabled: true," +
                "                distance: -25," +
                "                format: '{point.percentage:.1f}%'," +
                "                style: {" +
                "                    fontSize: '0.8em'," +
                "                    textOutline: 'none'," +
                "                    opacity: 0.7" +
                "                }," +
//                "                filter: {" +
//                "                    operator: '>'," +
//                "                    property: 'percentage'," +
//                "                    value: 10" +
//                "                }" +
                "            }]" +
                "        }" +
                "    }," +
                "    series: [" +
                "        {" +
                "            minPointSize: 10," +
                "            innerSize: '60%'," +
                "            zMin: 0," +
                "            borderRadius: 5," +
                "            name: 'Total'," +
                "            colorByPoint: true," +
                "            data: [" + seriesfinal +
                "            ]" +
                "        }" +
                "    ]" +
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

    private Number calculateIncrementPorc(Number valueStar, Number valueEnd) {
        if (valueStar == null || valueEnd == null) {
            return 0;
        }
        return ((valueEnd.doubleValue() - valueStar.doubleValue()) / valueStar.doubleValue()) * 100;
    }

    private Number calculateIncrement(Number valueStar, Number valueEnd) {
        if (valueStar == null || valueEnd == null) {
            return 0;
        }
        return ((valueEnd.doubleValue() - valueStar.doubleValue()));
    }


    @Override
    protected void onAttach(AttachEvent attachEvent) {
        updateLineChart();
        updateColumnChart();
        updatePieChart();
    }

}
