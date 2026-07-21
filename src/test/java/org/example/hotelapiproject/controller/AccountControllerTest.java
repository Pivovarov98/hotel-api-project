package org.example.hotelapiproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.hotelapiproject.dto.account_dto.AccountCreateDTO;
import org.example.hotelapiproject.dto.account_dto.AccountResponseDTO;
import org.example.hotelapiproject.dto.account_dto.AccountUpdateDTO;
import org.example.hotelapiproject.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {

    @MockitoBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registration() throws Exception {

        AccountCreateDTO createDTO = AccountCreateDTO.builder()
                .email("Test@test.com")
                .name("Egor")
                .surname("Piv")
                .password("test123")
                .repeatPassword("test123")
                .isHotelOwner(false)
                .build();

        AccountResponseDTO response = AccountResponseDTO.builder()
                .email(createDTO.getEmail())
                .surname(createDTO.getSurname())
                .name(createDTO.getName())
                .build();

        when(accountService.registration(any()))
                .thenReturn(response);

        mockMvc.perform(post("/accounts/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.surname").value(response.getSurname()))
                .andExpect(jsonPath("$.email").value(response.getEmail()));
    }

    @Test
    void auth() throws Exception {
    }

    @Test
    void findAccountByID() throws Exception {

        AccountResponseDTO response = AccountResponseDTO.builder()
                .email("Test@test.com")
                .surname("Piv")
                .name("Egor")
                .build();

        when(accountService.findAccountByID(anyLong()))
                .thenReturn(response);

        mockMvc.perform(get("/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.surname").value(response.getSurname()))
                .andExpect(jsonPath("$.email").value(response.getEmail()));
    }

    @Test
    void updateAccount() throws Exception {

        AccountUpdateDTO updateDTO = AccountUpdateDTO.builder()
                .email("newtest@test.com")
                .name("Test name")
                .surname("Test surname")
                .build();

        AccountResponseDTO response = AccountResponseDTO.builder()
                .email(updateDTO.getEmail())
                .surname(updateDTO.getSurname())
                .name(updateDTO.getName())
                .build();

        when(accountService.updateAccountByID(anyLong(), any()))
                .thenReturn(response);

        mockMvc.perform(patch("/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.surname").value(response.getSurname()))
                .andExpect(jsonPath("$.email").value(response.getEmail()));
    }

    @Test
    void changePassword() throws Exception {
    }

    @Test
    void deleteAccountByID() throws Exception {

        mockMvc.perform(delete("/accounts/1"))
                .andExpect(status().isNoContent());

        verify(accountService).deleteAccountByID(1L);
    }
}