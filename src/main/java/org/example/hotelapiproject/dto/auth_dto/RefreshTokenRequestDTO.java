package org.example.hotelapiproject.dto.auth_dto;

import jakarta.validation.constraints.NotBlank;

public class RefreshTokenRequestDTO {

    @NotBlank
    private String refreshToken;
}
