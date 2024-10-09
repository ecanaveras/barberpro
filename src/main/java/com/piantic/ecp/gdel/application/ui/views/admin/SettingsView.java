package com.piantic.ecp.gdel.application.ui.views.admin;

import com.piantic.ecp.gdel.application.backend.entity.setting.ConfigOption;
import com.piantic.ecp.gdel.application.backend.service.setting.ConfigOptionService;
import com.piantic.ecp.gdel.application.backend.utils.MessagesUtil;
import com.piantic.ecp.gdel.application.ui.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.Optional;

@PageTitle("Configuración")
@Route(value = "settings", layout = MainLayout.class)
public class SettingsView extends Div implements BeforeLeaveObserver {

    private ConfigOptionService optionService;

    public SettingsView(ConfigOptionService optionService) {
        addClassName("settings-view");
        addClassNames(LumoUtility.Padding.MEDIUM);

        add(new HorizontalLayout(new Span(LineAwesomeIcon.TOOLS_SOLID.create()), new H3("Preferencias")), new H5("Configuración de la aplicación"));
        this.optionService = optionService;
        optionService.populateDefaultData();

        TabSheet tabSheet = new TabSheet();
        tabSheet.add(new Span("General"), createUIOptions());
        tabSheet.add(new Span("Perfiles"), createUIPerfiles());
        tabSheet.add(new Span("Actividades"), createUIActividades());
        tabSheet.add(new Span("Subscripción"), createUISubscription());
        tabSheet.add(new Span("Feedback"), createUIFeedback());
//        tabSheet.add(new Span("Dispositivos"), createUIFeedback());
//        tabSheet.add(new Span("Administradores"), createUIFeedback());
//        tabSheet.add(new Span("Exportar Info"), createUIFeedback());
//        tabSheet.add(new Span("Notificaciones"), createUIFeedback());
        add(tabSheet);
    }



    private Component createUIOptions() {
        VerticalLayout content = new VerticalLayout();
        content.addClassName("content-tab-options");
        content.setPadding(false);

//        content.add(MessagesUtil.showWarning("Configura las opciones que mejor se adecuen a tus necesidades"));

        //MODO ASISTIDO
        VerticalLayout cardModoAsistido = getCardLayout("MODO ASISTIDO");

        //Option 1
        VerticalLayout optionItem1 = optionLayout(ConfigOptionService.ENABLE_OPTION_ASISTIDO);
        Checkbox checkboxModoAsistido = getCheckComponent("Activar modo Asistido", ConfigOptionService.ENABLE_OPTION_ASISTIDO);
        optionItem1.add(checkboxModoAsistido);

        //Option 2
        VerticalLayout optionItem2 = optionLayout(ConfigOptionService.ENABLE_PIN_ASISTIDO);

        Optional<ConfigOption> optionEnablePIN = optionService.findByName(ConfigOptionService.ENABLE_PIN_ASISTIDO);
        Optional<ConfigOption> optionPIN = optionService.findByName(ConfigOptionService.VALUE_PIN_ASISTIDO);
        Checkbox checkboxPIN = getCheckComponent("Solicitar PIN en el modo asistido", ConfigOptionService.ENABLE_PIN_ASISTIDO);

        PasswordField passwordField = new PasswordField("PIN Modo Asisistido");
        passwordField.setHelperText("Si el pin está habilitado, el usuario tendra que introducirlo para acceder al modo asistido");
        passwordField.setEnabled(checkboxPIN.getValue());
        passwordField.setMinLength(4);
        passwordField.setMaxLength(4);
        passwordField.setRequired(checkboxPIN.getValue());
        passwordField.setRequiredIndicatorVisible(checkboxPIN.getValue());
        optionPIN.ifPresent(option -> {
            if (option.getConfigvalue() != null) {
                passwordField.setValue(option.getConfigvalue());
            }
        });

        Span infochangePIN = new Span();
        infochangePIN.getElement().getThemeList().add("badge success primary");
        infochangePIN.setVisible(false);

        passwordField.addValueChangeListener(event -> {
            if (event.getValue() == null || event.getValue().isEmpty() || event.getValue().length() < 4) {
                infochangePIN.setVisible(false);
                return;
            }
            ConfigOption option = optionPIN.get();
            option.setConfigvalue(event.getValue());
            optionService.save(option);
            infochangePIN.setVisible(true);
            infochangePIN.setText("PIN Actualizado!");
        });


        //Enable UI
        if (!checkboxModoAsistido.getValue()) {
            checkboxPIN.setEnabled(false);
            passwordField.setEnabled(false);
            checkboxModoAsistido.setEnabled(true);
        }
        checkboxModoAsistido.addValueChangeListener(event -> {
            checkboxPIN.setEnabled(event.getValue());
            passwordField.setEnabled(event.getValue() && checkboxPIN.getValue());
            checkboxModoAsistido.setEnabled(true);
        });

        HorizontalLayout passLayout = new HorizontalLayout();
        passLayout.addClassNames(LumoUtility.Display.FLEX, LumoUtility.AlignContent.CENTER, LumoUtility.AlignItems.CENTER, LumoUtility.Padding.Left.LARGE);
        passLayout.add(passwordField, infochangePIN);


        checkboxPIN.addValueChangeListener(event -> {
            passwordField.setEnabled(event.getValue());
            ConfigOption option = optionEnablePIN.get();
            option.setConfigvalue(event.getValue() ? "true" : "false");
            optionService.save(option);
        });

        optionItem2.add(checkboxPIN, passLayout);

        cardModoAsistido.add(optionItem1, optionItem2);

        content.add(cardModoAsistido);

        return content;
    }

    private Component createUIPerfiles() {
        VerticalLayout content = new VerticalLayout();
        content.addClassName("content-tab-profiles");
        content.setPadding(false);

        //Perfil puede borrar actividades propias
        VerticalLayout card = getCardLayout("OPCIONES DE PERFIL");

        //Option 1
        VerticalLayout optionItem1 = optionLayout(ConfigOptionService.ENABLE_OPTION_PERFIL_DELETE_WORK);
        optionItem1.addClassNames(LumoUtility.Gap.XSMALL);
        Checkbox chpermitir = getCheckComponent("Permitir borrar actividades propias", ConfigOptionService.ENABLE_OPTION_PERFIL_DELETE_WORK);

        Div hori = MessagesUtil.showWarning("Esta configuración solo aplica, si es permitido borrar actividades (menú actividades) y además está habilitada la actividad");
        hori.addClassNames(LumoUtility.Padding.XSMALL, LumoUtility.Margin.Left.LARGE);
        optionItem1.add(chpermitir, hori);


        card.add(optionItem1);

        content.add(card);

        return content;
    }

    private Component createUIActividades() {
        VerticalLayout content = new VerticalLayout();
        content.addClassName("content-tab-actvidades");
        content.setPadding(false);

        //Perfil puede borrar actividades propias
        VerticalLayout card = getCardLayout("OPCIONES DE ACTIVIDADES");

        //Option 1
        VerticalLayout optionItem1 = optionLayout(ConfigOptionService.ENABLE_OPTION_ACTIVIDAD_DELETE_WORK);
        Checkbox chpermitir = getCheckComponent("Permitir borrar actividades", ConfigOptionService.ENABLE_OPTION_ACTIVIDAD_DELETE_WORK);

        //Option 2
        VerticalLayout optionItem2 = optionLayout(ConfigOptionService.ENABLE_OPTION_ACTIVIDAD_BLOQUEO);
        Checkbox chpermitir2 = getCheckComponent("Bloquear actividades que hayan terminado hace más de 1 hora (No permite borrarlas)", ConfigOptionService.ENABLE_OPTION_ACTIVIDAD_BLOQUEO);
        chpermitir2.setEnabled(chpermitir.getValue());

        chpermitir.addValueChangeListener(event -> {
            chpermitir2.setEnabled(event.getValue());
            optionService.findByName(ConfigOptionService.ENABLE_OPTION_ACTIVIDAD_DELETE_WORK).ifPresent(option -> {
               option.setConfigvalue(event.getValue() ? "true" : "false");
               optionService.save(option);
            });
        });

        chpermitir2.addValueChangeListener(event -> {
            optionService.findByName(ConfigOptionService.ENABLE_OPTION_ACTIVIDAD_BLOQUEO).ifPresent(option -> {
               option.setConfigvalue(event.getValue() ? "true" : "false");
               optionService.save(option);
            });
        });

        optionItem1.add(chpermitir);
        optionItem2.add(chpermitir2);

        card.add(optionItem1, optionItem2);

        content.add(card);

        return content;
    }

    private Component createUISubscription() {
        VerticalLayout layout = new VerticalLayout();
        H5 title = new H5("Subscripción");

        VerticalLayout content = new VerticalLayout();
        layout.add(title, content);

        content.add(new Span("Tienes una suscripción activa"));
        Span span = new Span("PRUEBA GRATUITA");
        span.getElement().getThemeList().add("badge success large");
        content.add(new Span("Tipo de licencia:"));
        content.add(span);
        return layout;
    }

    private Component createUIFeedback() {
        VerticalLayout layout = new VerticalLayout();
        H5 title = new H5("Feedback");

        VerticalLayout content = new VerticalLayout();
        layout.add(title, content);

        content.add(new Span("Pronto podrás enviarnos tus comentarios y sugerencias"));

        return layout;
    }

    private VerticalLayout getCardLayout(String cardName) {
        VerticalLayout cardlayout = new VerticalLayout();
        cardlayout.addClassNames(LumoUtility.Gap.SMALL,
                LumoUtility.Padding.LARGE,
                LumoUtility.BorderRadius.SMALL,
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST_10);

        cardlayout.add(new H5(cardName));

        return cardlayout;
    }

    private Checkbox getCheckComponent(String optionLabel, String optionFind) {
        Checkbox checkbox = new Checkbox(optionLabel);
        Optional<ConfigOption> optiondb = optionService.findByName(optionFind);
        optiondb.ifPresent(option -> {
            checkbox.setValue(option.getConfigvalue().equals("true"));
            checkbox.setHelperText(option.getDescription());
        });
        checkbox.addValueChangeListener(event -> {
            ConfigOption option = optiondb.get();
            option.setConfigvalue(event.getValue() ? "true" : "false");
            optionService.save(option);
        });
        return checkbox;
    }

    private VerticalLayout optionLayout(String optionId){
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setId(optionId);
        layout.setVisible(false);
        optionService.findByName(optionId).ifPresent(option -> {
            layout.setVisible(true);
        });
        return layout;
    }


    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        // TODO: Cargar la configuración a la Session activa.
    }
}

