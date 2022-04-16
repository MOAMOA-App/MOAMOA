package com.example.moamoa.ui.dashboard;

import java.util.ArrayList;

public class DashboardData {
    String formname;
    String recentmessage;

    public DashboardData(String formname, String recentmessage) {
        this.formname = formname;
        this.recentmessage= recentmessage;
    }


    public String getMessage() {
        return recentmessage;
    }

    public void setMessage(String recentmessage) { this.recentmessage = recentmessage; }


    public String getName() {
        return formname;
    }

    public void setName(String formname) {
        this.formname = formname;
    }


    public static ArrayList<DashboardData> createContactsList(int numContacts) {
        ArrayList<DashboardData> contacts = new ArrayList<DashboardData>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new DashboardData( "FORMNAME","RECENTMESSAGE"));
        }
        return contacts;
    }
}