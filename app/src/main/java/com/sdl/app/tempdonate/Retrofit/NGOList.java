package com.sdl.app.tempdonate.Retrofit;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NGOList {

    private boolean success;
    private String msg;
    private String name;
    private String Address;
    private int amount;
    private String userTo;
    private String userFrom;
    @SerializedName("quantity")
    private int number;
    private ArrayList<String> items;
    @SerializedName("issDate")
    private String date;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUserTo() {
        return userTo;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public String getDate() throws ParseException {
        String oldDateString, newDateString;
        String old_format = "yyyy-MM-dd";
        String new_format = "dd-MM-yyyy";
        oldDateString = date.substring(0,10);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(old_format);
        Date date = simpleDateFormat.parse(oldDateString);
        simpleDateFormat.applyPattern(new_format);
        newDateString = simpleDateFormat.format(date);
        return newDateString;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
