package com.ecommerce.user_workspace.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private Long id;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipcode;

}
