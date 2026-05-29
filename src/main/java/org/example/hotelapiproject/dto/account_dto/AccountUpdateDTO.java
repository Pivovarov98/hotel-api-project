package org.example.hotelapiproject.dto.account_dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class AccountUpdateDTO {

    @Email(message = "Invalid email format")
    private String email;

    private String name;
    private String surname;
}
