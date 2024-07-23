package com.piantic.ecp.gdel.application.ui.views;

import com.piantic.ecp.gdel.application.ui.views.forms.ActivityDetailForm;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

@Route("feed")
public class ActivityView extends VerticalLayout implements HasUrlParameter<String> {


    private ActivityDetailForm activityDetailForm = new ActivityDetailForm();

    public ActivityView() {
        addClassName("activity-view");

        add(createContentLayout(1));
        add(createContentLayout(2));
        add(createContentLayout(3));
        add(createContentLayout(4));
        add(createContentLayout(5));
        add(createContentLayout(6));
        add(createContentLayout(7));

        add(activityDetailForm);

    }

    private void addEvents(VerticalLayout form) {
        form.getUI().ifPresent(ui -> ui.navigate(ActivityView.class, form.getId().toString()));
    }

    private VerticalLayout createContentLayout(Integer id) {
        VerticalLayout content = new VerticalLayout();
        content.setId(String.valueOf(id));
        content.addClassName("content-feed");
        content.addSingleClickListener(e -> {
            addEvents(e.getSource());
        });

        Span span = new Span(String.valueOf(id));
        span.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.TextColor.PRIMARY);
        Span span1 = new Span("Corte");
        span1.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.FontWeight.SEMIBOLD);
        Span span2 = new Span("Vie, Jul 19, 2024");
        span2.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.LIGHT);

        HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setWidthFull();
        hLayout.addClassNames(LumoUtility.BorderRadius.MEDIUM, LumoUtility.BorderColor.CONTRAST_20, LumoUtility.Background.CONTRAST_10, LumoUtility.Padding.SMALL, LumoUtility.Flex.GROW);
        hLayout.add(new HorizontalLayout(LineAwesomeIcon.USER_CIRCLE.create(), new Span("Perfil TOruMacto")), new Span("25k"), LineAwesomeIcon.PLUS_SOLID.create());

        content.add(span, span1, span2, hLayout);

        content.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.Margin.Bottom.SMALL, LumoUtility.BorderRadius.LARGE, LumoUtility.Background.BASE, LumoUtility.BorderColor.CONTRAST_5, LumoUtility.Border.ALL);


        return content;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String s) {
        if (s != null) {
            if (activityDetailForm != null && activityDetailForm.getID() != null && activityDetailForm.getID().equals(s) && activityDetailForm.getClassNames().contains("visible")) {
                activityDetailForm.removeClassName("visible");
                activityDetailForm.setID(null);
                return;
            }
            activityDetailForm.addClassName("visible");
            activityDetailForm.setID(s);
            activityDetailForm.updateUI();
        }
    }
}

