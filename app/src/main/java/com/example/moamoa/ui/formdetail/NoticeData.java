package com.example.moamoa.ui.formdetail;

public class NoticeData {
    private String subject;
    private String text;
    private String date;
    private int side;
    private String fid;
    private int numb;

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public int getSide() {
        return side;
    }

    public String getFid() {
        return fid;
    }

    public int getNumb() {
        return numb;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public void setNumb(int numb) {
        this.numb = numb;
    }
}
