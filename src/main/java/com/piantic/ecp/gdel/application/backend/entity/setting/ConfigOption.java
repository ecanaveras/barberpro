package com.piantic.ecp.gdel.application.backend.entity.setting;

import com.piantic.ecp.gdel.application.backend.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity(name = "_config_options")
public class ConfigOption extends BaseEntity {

    @NotNull
    private String name;

    private String description;

    @Column(name = "config_value", nullable = true)
    private String configvalue;

    public ConfigOption() {
    }

    public ConfigOption(String name, String description, String configvalue) {
        this.name = name;
        this.description = description;
        this.configvalue = configvalue;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        if (description == null) {
            return "";
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConfigvalue() {
        return configvalue;
    }

    public void setConfigvalue(String configvalue) {
        this.configvalue = configvalue;
    }
}
