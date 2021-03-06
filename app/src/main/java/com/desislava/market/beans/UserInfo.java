package com.desislava.market.beans;

import java.io.Serializable;

/**
 * Represent a single user that will be stored in Database, to be able to load data on given username.
 * !! MIGHT ALSO BE useful to store order for given user....
 */

public class UserInfo implements Serializable {

    private String email;
    private String name;
    private String username;
    private String password;
    private String phone;

    //Ship address

    //TODO missing field
    private String address;
    private String district;
    private String city;

    private String setFullAddress; //When location is taken from current location

    public UserInfo() {

    }

    public UserInfo(String email, String name, String username, String password, String phone, String address, String district, String city) {
        this.email = email;
        this.name = name;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.district = district;
        this.city = city;
    }

    public UserInfo(UserInfo info) {

        //TODO parse the given bean
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getFullAddress(){
        return setFullAddress;
    }

    public void setSetFullAddress(String setFullAddress) {
        this.setFullAddress = setFullAddress;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", district='" + district + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
