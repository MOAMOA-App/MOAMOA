package com.example.moamoa.ui.notifications;

import java.util.ArrayList;

public class NotificationsData {
    String alarmmessage;
    String formname;

    public NotificationsData(String alarmmessage, String formname) {
        this.alarmmessage = alarmmessage;
        this.formname= formname;
    }

    public String getName() {
        return alarmmessage;
    }

    public void setName(String alarmmessage) {
        this.alarmmessage = alarmmessage;
    }

    public String getMessage() {
        return formname;
    }

    public void setMessage(String formname) { this.formname = formname; }

    public static ArrayList<NotificationsData> createContactsList(int numContacts) {
        ArrayList<NotificationsData> contacts = new ArrayList<NotificationsData>();

        return contacts;
    }
}