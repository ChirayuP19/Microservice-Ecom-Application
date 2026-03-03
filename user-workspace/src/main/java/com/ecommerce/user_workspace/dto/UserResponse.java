package com.ecommerce.user_workspace.dto;


import com.ecommerce.user_workspace.entity.UserRole;
import lombok.Data;

@Data
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private UserRole role;
    private AddressDTO addressDTO;
}
