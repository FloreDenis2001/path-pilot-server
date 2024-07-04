package com.mycode.pathpilotserver.city.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class City {
    private String city;
    private double lat;
    private double lng;
    private String country;
    private String iso2;
    private String admin_name;
    private String capital;
    private String population;
    private String population_proper;

    @Override
    public String toString() {
        return "City{" +
                "city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", iso2='" + iso2 + '\'' +
                ", admin_name='" + admin_name + '\'' +
                ", capital='" + capital + '\'' +
                ", population='" + population + '\'' +
                ", population_proper='" + population_proper + '\'' +
                '}';
    }
}
