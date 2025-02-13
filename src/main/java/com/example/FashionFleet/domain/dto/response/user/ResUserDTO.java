package com.example.FashionFleet.domain.dto.response.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUserDTO {
    private long id;
    private String name;
    private String address;
    private String email;
    private String phoneNumber;
    private int age;
}
