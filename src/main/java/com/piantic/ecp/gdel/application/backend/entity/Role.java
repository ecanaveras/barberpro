package com.piantic.ecp.gdel.application.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Role extends AbstractEntity {

    @NotNull
    @NotEmpty
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_work",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "work_id")
    )
    private Set<Work> works = new HashSet<>();


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_profile",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id")
    )
    private Set<Profile> profiles = new HashSet<>();

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Work> getWorks() {
        return works;
    }

    public void setWorks(Set<Work> works) {
        this.works = works;
    }

    public int getWorksCount() {
        return works.size();
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }

    public int getProfilesCount() {
        return profiles.size();
    }

}
