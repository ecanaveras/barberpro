package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.Application;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;

@JsModule("https://code.highcharts.com/highcharts.js")
@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends Div {

    public DashboardView() {
        addClassName("dashboard-view");

        H1 title = new H1("Dashboard");
        Span span = new Span("Dashboard en construcción");

        setTitle(Application.getTenand().getNameTenant());

        add(title);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        getElement().executeJs("this.innerHTML = '<div id=\"container\" style=\"width:100%; height:400px;\"></div>';" +
                "Highcharts.chart('container', {" +
                "    chart: {" +
                "        type: 'line'" +
                "    }," +
                "    title: {" +
                "        text: 'My Highchart'" +
                "    }," +
                "    xAxis: {" +
                "        categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']" +
                "    }," +
                "    yAxis: {" +
                "        title: {" +
                "            text: 'Values'" +
                "        }" +
                "    }," +
                "    series: [{" +
                "        name: 'Sample Data'," +
                "        data: [29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]" +
                "    }]" +
                "});");
    }
}
