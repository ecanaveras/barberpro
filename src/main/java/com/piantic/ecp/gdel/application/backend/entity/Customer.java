package com.piantic.ecp.gdel.application.backend.entity;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.type.TrueFalseConverter;

@Entity
public class Customer extends AbstractEntity implements Cloneable {

    @NotNull
    @NotEmpty
    private String name;

    private String phone;

    @Convert(converter = TrueFalseConverter.class)
    private Boolean favorite;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean isFavorite() {
        return favorite != null && favorite;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

}
