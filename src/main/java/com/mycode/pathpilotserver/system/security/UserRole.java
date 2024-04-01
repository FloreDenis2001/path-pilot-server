package com.mycode.pathpilotserver.system.security;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum UserRole {

    ADMIN(Sets.newHashSet(UserPermisson.ADMIN_ADD, UserPermisson.ADMIN_UPDATE, UserPermisson.ADMIN_DELETE)),
    CUSTOMER(Sets.newHashSet(UserPermisson.CUSTOMER_ADD, UserPermisson.CUSTOMER_UPDATE, UserPermisson.CUSTOMER_DELETE)),
    DRIVER(Sets.newHashSet(UserPermisson.DRIVER_ADD, UserPermisson.DRIVER_UPDATE, UserPermisson.DRIVER_DELETE));



    private final Set<UserPermisson> permissions;

    public Set<UserPermisson> getPermissions() {
        return permissions;
    }


    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return permissions;
    }
}
