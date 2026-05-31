package org.example.hotelapiproject.dto.auth_dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "The Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "The Email cannot be blank")
    private String password;
}
