package com.mycode.pathpilotserver.address.models;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Address {

    private String country;
    private String city;
    private double lat;
    private double lng;
    private String iso2;
    private String admin_name;
    private String capital;
    private String population;
    private String population_proper;
    private String street;
    private String streetNumber;
    private String postalCode;



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
