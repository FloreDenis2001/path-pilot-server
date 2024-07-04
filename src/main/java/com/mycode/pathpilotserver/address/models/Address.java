package com.mycode.pathpilotserver.address.models;

import com.mycode.pathpilotserver.city.models.City;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Address{

    @Embedded
    private City cityDetails;
    private String street;
    private String streetNumber;
    private String postalCode;


    @Override
    public String toString() {
        return "Address{" +
                "city='" + cityDetails.getCity() + '\'' +
                ", lat=" + cityDetails.getLat() +
                ", lng=" + cityDetails.getLng() +
                ", country='" + cityDetails.getCountry() + '\'' +
                ", iso2='" + cityDetails.getIso2() + '\'' +
                ", admin_name='" + cityDetails.getAdmin_name() + '\'' +
                ", capital='" + cityDetails.getCapital() + '\'' +
                ", population='" + cityDetails.getPopulation() + '\'' +
                ", population_proper='" + cityDetails.getPopulation_proper() + '\'' +
                ", street='" + street + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
