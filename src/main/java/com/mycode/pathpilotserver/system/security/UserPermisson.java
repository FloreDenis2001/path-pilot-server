package com.mycode.pathpilotserver.system.security;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserPermisson {
    ADMIN_ADD("admin:add"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),

    CUSTOMER_ADD("customer:add"),

    CUSTOMER_UPDATE("customer:update"),

    CUSTOMER_DELETE("customer:delete"),

    DRIVER_ADD("driver:add"),

    DRIVER_UPDATE("driver:update"),

    DRIVER_DELETE("driver:delete");


    private final String permission;



    public String getPermission() {
        return permission;
    }


}
