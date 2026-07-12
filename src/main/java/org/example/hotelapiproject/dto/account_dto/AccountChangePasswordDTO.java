package org.example.hotelapiproject.dto.account_dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountChangePasswordDTO {

    @NotBlank(message = "Please enter an old password")
    private String oldPassword;

    @NotBlank(message = "Please enter a new password")
    private String newPassword;

    @NotBlank(message = "Please repeat new password")
    private String repeatNewPassword;
}
