package org.example.hotelapiproject.dto.account_dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountCreateDTO {

    @NotBlank
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank
    private String name;

    private String surname;

    @NotBlank
    private String password;

    @NotBlank
    private String repeatPassword;


    private boolean isHotelOwner;
}
