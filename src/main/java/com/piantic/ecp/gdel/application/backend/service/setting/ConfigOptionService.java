package com.piantic.ecp.gdel.application.backend.service.setting;


import com.piantic.ecp.gdel.application.Application;
import com.piantic.ecp.gdel.application.backend.entity.setting.ConfigOption;
import com.piantic.ecp.gdel.application.backend.repository.setting.ConfigOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ConfigOptionService {

    //MODO ASISITDO
    public static String ENABLE_OPTION_ASISTIDO = "option.asistido.enabled";
    public static String ENABLE_PIN_ASISTIDO = "option.asistido.pin.enabled";
    public static String VALUE_PIN_ASISTIDO = "option.asistido.pin.value";

    //OPCIONES DE PERFIL
    public static String ENABLE_OPTION_PERFIL_DELETE_WORK = "option.perfil.delete.work.enabled";

    //OPCIONES DE ACTIVIDADES
    public static String ENABLE_OPTION_ACTIVIDAD_DELETE_WORK = "option.actividades.delete.work.enabled";
    public static String ENABLE_OPTION_ACTIVIDAD_BLOQUEO = "option.actividades.lock.work.enabled";



    @Autowired
    private ConfigOptionRepository optionRepository;


    public Optional<ConfigOption> findByName(String name) {
        return optionRepository.findByTenantAndNameAndEnabledTrue(Application.getTenant(), name);
    }

    public void save(ConfigOption option) {
        optionRepository.save(option);
    }

    //LOAD DATA DEFAULT
//    @PostConstruct
    public void populateDefaultData() {
        //TODO: Llamar metodo luego del login (Tenant establecido)
        if (optionRepository.count() == 0) {
            //Opciones por defecto
            ArrayList<ConfigOption> options = new ArrayList<>();
            ConfigOption opteoa = new ConfigOption(ENABLE_OPTION_ASISTIDO, "Permite usar el modo asistido en la aplicación.", "false");
            ConfigOption optepa = new ConfigOption(ENABLE_PIN_ASISTIDO, "Solicitar PIN en el modo asistido.", "false");
            ConfigOption optvps = new ConfigOption(VALUE_PIN_ASISTIDO, "PIN para el modo asistido.", null);
            options.add(opteoa);
            options.add(optepa);
            options.add(optvps);

            //Perfiles
            ConfigOption opteopdw = new ConfigOption(ENABLE_OPTION_PERFIL_DELETE_WORK, "Permite que el perfil pueda borrar tareas realizadas por el mismo.", "false");
            options.add(opteopdw);

            //Actividades
            ConfigOption opteoadw = new ConfigOption(ENABLE_OPTION_ACTIVIDAD_DELETE_WORK, "Permite la eliminación de una actividad.", "false");
            ConfigOption opteoab = new ConfigOption(ENABLE_OPTION_ACTIVIDAD_BLOQUEO, "Bloquea las actividades que hayan finazado hace más de 1 hora.", "false");
            options.add(opteoadw);
            options.add(opteoab);

            //Guardar all
            optionRepository.saveAll(options);
        }
    }
}
