package com.example.moamoa.ui.formdetail;

public class NoticeData {
    private String subject;
    private String text;
    private String date;

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(String date) {
        date = date.substring(2,4)+"/"+date.substring(4,6)+"/"+date.substring(6,8)+" "+date.substring(8,10)+":"+date.substring(10,12);
        this.date = date;
    }


}
