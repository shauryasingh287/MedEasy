package com.example.medicalrecord;

import java.util.ArrayList;

public class family {
    ArrayList<String> med;
    String s;
    public  family()
    {

    }

    public  family(String s)
    {
        this.s=s;
    }
    public String getS()
    {
        return s;
    }

    public family(ArrayList<String> med1) {
        med=med1;
    }

    public ArrayList<String> getMed() {
        return med;
    }

}
