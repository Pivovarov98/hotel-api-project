package org.example.hotelapiproject.dto.auth_dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDTO {

    @NotBlank(message = "The Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "The Email cannot be blank")
    private String password;
}
