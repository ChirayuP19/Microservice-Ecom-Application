package com.ecommerce.user_workspace.dto;

import lombok.Data;

@Data
public class UserRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private AddressDTO address;
}
