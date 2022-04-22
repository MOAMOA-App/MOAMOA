package com.example.moamoa.ui.formcreate;

public class SingerItem {
    String name;
    String address;
    String title;
    int price;
    int resId;

    //생성
    public SingerItem(String name, String address, String title, int price, int resId) {
        this.name = name;
        this.address = address;
        this.title = title;
        this.price = price;
        this.resId = resId;
    }

    //변수에 접근할 때 .OO 접근하기보다는 안전하게 getter, setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getPrice() {
        return String.valueOf(price);
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getResId() {
        return resId;
    }

    @Override
    public String toString() {
        return "SingerItem{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}