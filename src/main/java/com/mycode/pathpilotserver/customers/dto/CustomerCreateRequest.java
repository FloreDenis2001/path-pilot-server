package com.mycode.pathpilotserver.customers.dto;

import com.mycode.pathpilotserver.address.Address;

public record CustomerCreateRequest(String name,String phone,Address address,String password,String username,String email) {
}
