package com.example.FashionFleet.domain.dto.response.user;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private String address;
    private String phoneNumber;
    private int age;
    private Instant createdAt;
}
