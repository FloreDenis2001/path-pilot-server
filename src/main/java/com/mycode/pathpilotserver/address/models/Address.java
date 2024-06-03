package com.mycode.pathpilotserver.address.models;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Setter
@Getter
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

    public static Address convertToAddress(String addressString) {
        String[] addressParts = addressString.split(",");
        if (addressParts.length < 5) {
            throw new IllegalArgumentException("Invalid address format");
        }
        String country = addressParts[0].trim();
        String city = addressParts[1].trim();
        String street = addressParts[2].trim();
        String streetNumber = addressParts[3].trim();
        String postalCode = addressParts[4].trim();

        return new Address(country, city, street, streetNumber, postalCode);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Address{");
        sb.append("country='").append(country).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", street='").append(street).append('\'');
        sb.append(", streetNumber='").append(streetNumber).append('\'');
        sb.append(", postalCode='").append(postalCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
