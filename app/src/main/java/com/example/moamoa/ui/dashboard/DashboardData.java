package com.example.moamoa.ui.dashboard;

import java.util.ArrayList;

public class DashboardData {
    String formname;
    String recentmessage;
    int resourceId;

    public DashboardData(int resourceId, String formname, String recentmessage) {
        this.formname = formname;
        this.recentmessage= recentmessage;
        this.resourceId = resourceId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getMessage() {
        return recentmessage;
    }

    public String getName() {
        return formname;
    }

    public void setMessage(String recentmessage) { this.recentmessage = recentmessage; }

    public void setName(String formname) {
        this.formname = formname;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public static ArrayList<DashboardData> createContactsList(int numContacts) {
        ArrayList<DashboardData> contacts = new ArrayList<DashboardData>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new DashboardData(123, "FORMNAME","RECENTMESSAGE"));
        }
        return contacts;
    }
}