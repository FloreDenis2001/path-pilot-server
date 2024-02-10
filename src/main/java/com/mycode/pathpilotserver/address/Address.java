package com.mycode.pathpilotserver.address;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {

    private String country;
    private String city;
    private String street;
    private String streetNumber;
    private String postalCode;

    public Address() {
    }

    public Address(String country, String city, String street, String streetNumber, String postalCode) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }
}
