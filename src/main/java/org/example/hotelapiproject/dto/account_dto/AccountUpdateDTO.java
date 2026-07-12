package org.example.hotelapiproject.dto.account_dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountUpdateDTO {

    @Email(message = "Invalid email format")
    private String email;

    private String name;
    private String surname;
}
