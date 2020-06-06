package org.example;

import java.io.Serializable;

public class Vertice implements Serializable {
    private Long id;
    private String value;

    public Vertice(Long id, String name) {
        this.id = id;
        this.value = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
