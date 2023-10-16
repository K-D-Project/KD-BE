package com.example.kdbe.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter @Setter
public class SignUpRequestDto {

    private String userId;
    private String password;
    private String email;
    private String role;

}
