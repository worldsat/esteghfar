package com.jamali.esteghfar70.Kernel.Controller.Domain;

public class Filter {
    private String field;
    private String value;


    public Filter(String field, String value) {
        this.field = field;
        this.value = value;

    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
