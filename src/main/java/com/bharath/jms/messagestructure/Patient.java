package com.bharath.jms.messagestructure;

import java.io.Serializable;

public class Patient implements Serializable {
    private  int id;
    private  String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Patient(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
