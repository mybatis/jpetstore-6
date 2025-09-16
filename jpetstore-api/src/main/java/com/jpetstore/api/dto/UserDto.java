package com.jpetstore.api.dto;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String country;
}
