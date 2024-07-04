package com.mycode.pathpilotserver.address.dto;

import com.mycode.pathpilotserver.address.models.Address;

public record AddressDTO(String city, String streetNumber, String street, String country, String postalCode) {

    public static AddressDTO from(Address address) {
        return new AddressDTO(address.getCityDetails().getCity(), address.getStreetNumber(), address.getStreet(), address.getCityDetails().getCountry(), address.getPostalCode());
    }
}
