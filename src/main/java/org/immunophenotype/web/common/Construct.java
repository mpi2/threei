package org.immunophenotype.web.common;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Construct {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    private  String  Construct;

    public Construct(String construct) {
        Construct = construct;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getConstruct() {
        return Construct;
    }

    public void setConstruct(String construct) {
        Construct = construct;
    }

}
