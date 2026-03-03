package com.app.ecom.dto;

import com.app.ecom.entity.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
