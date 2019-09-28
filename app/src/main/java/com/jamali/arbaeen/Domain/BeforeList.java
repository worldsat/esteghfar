package com.jamali.arbaeen.Domain;

import com.jamali.arbaeen.Kernel.Controller.Domain.BaseDomain;

public class BeforeList extends BaseDomain {
    private String Id;
    private String Subject;

    public BeforeList() {
        setTableName("list_1");
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }
}
