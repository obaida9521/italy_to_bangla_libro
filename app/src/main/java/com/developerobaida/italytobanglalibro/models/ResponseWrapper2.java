package com.developerobaida.italytobanglalibro.models;

import java.util.ArrayList;

public class ResponseWrapper2 {
    String status;
    ArrayList<Model2> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Model2> getData() {
        return data;
    }

    public void setData(ArrayList<Model2> data) {
        this.data = data;
    }
}
