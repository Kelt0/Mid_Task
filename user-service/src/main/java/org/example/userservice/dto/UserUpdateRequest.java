package org.example.userservice.dto;

import java.util.Set;

public class UserUpdateRequest {
    private String email;
    private Set<String> roleNames;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Set<String> getRoleNames() { return roleNames; }
    public void setRoleNames(Set<String> roleNames) { this.roleNames = roleNames; }
}
