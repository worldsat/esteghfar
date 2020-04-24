package com.jamali.esteghfar70.Domain;

import com.jamali.esteghfar70.Kernel.Controller.Domain.BaseDomain;

public class ShowList extends BaseDomain {
    private String Id;
    private String Subject;
    private String Kind;

    public ShowList() {
        setTableName("1_1");
    }

    public String getKind() {
        return Kind;
    }

    public void setKind(String kind) {
        Kind = kind;
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
