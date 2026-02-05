package com.example.myapplication;
import java.io.Serializable;
public class User implements Serializable {
  private    String login;
    private String password;
    private String name;
    private int Id;
public User(String username,String password,String name){
    this.login=username;
    this.password=password;
    this.name=name;

}
    public String getName(){return name;}
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}

