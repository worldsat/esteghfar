package com.jamali.esteghfar70.Domain;

import com.jamali.esteghfar70.Kernel.Controller.Domain.BaseDomain;

public class AfterList extends BaseDomain {
    private String Id;
    private String Subject;

    public AfterList() {
        setTableName("list_3");
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
