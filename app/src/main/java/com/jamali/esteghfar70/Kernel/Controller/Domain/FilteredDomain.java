package com.jamali.esteghfar70.Kernel.Controller.Domain;


import androidx.annotation.NonNull;

public class FilteredDomain {
    @NonNull
    private String id;
    private String value;

    public FilteredDomain(@NonNull String id, String value) {
        this.id = id;
        this.value = value;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
