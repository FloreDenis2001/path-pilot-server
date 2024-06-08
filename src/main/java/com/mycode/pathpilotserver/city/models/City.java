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
        final StringBuilder sb = new StringBuilder("City{");
        sb.append("city='").append(city).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append(", iso2='").append(iso2).append('\'');
        sb.append(", admin_name='").append(admin_name).append('\'');
        sb.append(", capital='").append(capital).append('\'');
        sb.append(", population='").append(population).append('\'');
        sb.append(", population_proper='").append(population_proper).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
