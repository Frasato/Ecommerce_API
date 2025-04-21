package com.papelariafrasato.api.models;

import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String address_id;

    private String street;
    private int number;
    private String city;
    private int CEP;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address() {
    }

    public Address(String address_id, String street, int number, String city, int CEP, User user) {
        this.address_id = address_id;
        this.street = street;
        this.number = number;
        this.city = city;
        this.CEP = CEP;
        this.user = user;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCEP() {
        return CEP;
    }

    public void setCEP(int CEP) {
        this.CEP = CEP;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
