package com.developerobaida.italytobanglalibro.models;

public class Model2 {
    String bangla,spelling,italian;

    public Model2(String bangla, String spelling, String italian) {
        this.bangla = bangla;
        this.spelling = spelling;
        this.italian = italian;
    }

    public String getBangla() {
        return bangla;
    }

    public void setBangla(String bangla) {
        this.bangla = bangla;
    }

    public String getSpelling() {
        return spelling;
    }

    public void setSpelling(String spelling) {
        this.spelling = spelling;
    }

    public String getItalian() {
        return italian;
    }

    public void setItalian(String italian) {
        this.italian = italian;
    }
}
