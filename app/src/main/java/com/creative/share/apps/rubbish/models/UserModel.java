package com.creative.share.apps.rubbish.models;

import java.io.Serializable;

public class UserModel implements Serializable {

    private String user_id;
    private String name;
    private String phone_code;
    private String phone;
    private String address;
    private String image;
    private String cover;
    private String email;
    private String password;
    private int city_id;
    private double latitude;
    private double longitude;
    private double salary;
    private int bonus;
    private int user_type;
    private String token_id;
    private int active;

    public UserModel() {
    }


    public UserModel(String user_id, String name, String phone_code, String phone, String address, String image, String cover, String email, String password, int city_id, double latitude, double longitude, double salary, int bonus, int user_type, String token_id, int active) {
        this.user_id = user_id;
        this.name = name;
        this.phone_code = phone_code;
        this.phone = phone;
        this.address = address;
        this.image = image;
        this.cover = cover;
        this.email = email;
        this.password = password;
        this.city_id = city_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.salary = salary;
        this.bonus = bonus;
        this.user_type = user_type;
        this.token_id = token_id;
        this.active = active;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
