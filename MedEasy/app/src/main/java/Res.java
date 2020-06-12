package com.example.medicalrecord;

import java.util.ArrayList;

public class Res {
    String name,addr,phone,age;
    public Res(String name,String addr,String phone,String age) {
        this.name = name;
        this.addr = addr;
        this.phone=phone;
        this.age=age;
    }



    public String getName() {
        return this.name;
    }
    public String getAddr() {
        return this.addr;
    }

    public String getPhone() {
        return phone;
    }

    public String getAge() {
        return age;
    }
}

