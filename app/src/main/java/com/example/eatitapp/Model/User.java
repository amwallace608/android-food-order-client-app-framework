package com.example.eatitapp.Model;

public class User {
    private String Name;
    private String Password;
    private String Phone;

    public User(){

    }

    public User(String name, String password){
        Name = name;
        Password = password;
    }

    public String getPassword() {
        return Password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name){
        Name = name;
    }

    public void setPassword(String password){
        Password = password;
    }

    public void setPhone(String phone){Phone = phone;}

    public String getPhone() {
        return Phone;
    }
}
