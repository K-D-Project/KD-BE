package com.example.kdbe.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignInResponseDto {

    private String authToken;
    private String refToken;

}
