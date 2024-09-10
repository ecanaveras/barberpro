package com.piantic.ecp.gdel.application.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
public class Activity extends BaseEntity implements Cloneable {

    @DateTimeFormat
    private Date dateActivity;

    @ManyToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    @NotEmpty
    @NotNull
    private String descActivity;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    private Double valueActivity;

    public Date getDateActivity() {
        return dateActivity;
    }

    public void setDateActivity(Date dateActivity) {
        this.dateActivity = dateActivity;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getDescActivity() {
        return descActivity;
    }

    public void setDescActivity(String descActivity) {
        this.descActivity = descActivity;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Double getValueActivity() {
        return valueActivity;
    }

    public void setValueActivity(Double valueActivity) {
        this.valueActivity = valueActivity;
    }
}
