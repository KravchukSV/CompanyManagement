package com.example.companymanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id = null;

    @NotNull
    private String name;

    @ManyToMany(mappedBy = "projects", cascade = CascadeType.REFRESH)
    @JsonIgnoreProperties("projects")
    private Set<Programmer> programmers = new HashSet<>();

    @ManyToMany(mappedBy = "projects", cascade = CascadeType.REFRESH)
    @JsonIgnoreProperties("projects")
    private Set<Manager> managers = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Programmer> getProgrammers() {
        return programmers;
    }

    public void setProgrammers(Set<Programmer> programmers) {
        this.programmers = programmers;
    }

    public Set<Manager> getManagers() {
        return managers;
    }

    public void setManagers(Set<Manager> managers) {
        this.managers = managers;
    }
}
