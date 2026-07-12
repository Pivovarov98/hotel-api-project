package org.example.hotelapiproject.service;

import org.example.hotelapiproject.dto.account_dto.AccountChangePasswordDTO;
import org.example.hotelapiproject.dto.account_dto.AccountCreateDTO;
import org.example.hotelapiproject.dto.account_dto.AccountResponseDTO;
import org.example.hotelapiproject.dto.account_dto.AccountUpdateDTO;
import org.example.hotelapiproject.dto.auth_dto.LoginRequestDTO;
import org.example.hotelapiproject.dto.auth_dto.LoginResponseDTO;
import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.exeption.account.AccountNotFoundException;
import org.example.hotelapiproject.exeption.account.PasswordNotMatchException;
import org.example.hotelapiproject.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private AccountService accountService;

    @Test
    void registration() {

        AccountCreateDTO dto = AccountCreateDTO.builder()
                .email("Test@test.com")
                .name("Test name")
                .surname("Test surname")
                .password("test123")
                .repeatPassword("test123")
                .isHotelOwner(false)
                .build();

        Account account = Account.builder()
                .id(1L)
                .email(dto.getEmail())
                .name(dto.getName())
                .surname(dto.getSurname())
                .hotels(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();

        when(accountRepository.save(any(Account.class)))
                .thenReturn(account);

        AccountResponseDTO responseDTO = accountService.registration(dto);

        assertEquals("Test@test.com", responseDTO.getEmail());
        assertEquals("Test name", responseDTO.getName());
        assertEquals("Test surname", responseDTO.getSurname());
    }

    @Test
    void registrationPasswordNotMatch() {

        AccountCreateDTO dto = AccountCreateDTO.builder()
                .email("Test@test.com")
                .name("Test name")
                .surname("Test surname")
                .password("test123")
                .repeatPassword("test1234")
                .isHotelOwner(false)
                .build();

        PasswordNotMatchException exception = assertThrows(
                PasswordNotMatchException.class,
                () -> accountService.registration(dto));

        assertEquals("The passwords do not match", exception.getMessage());

        verify(accountRepository, never())
                .save(any(Account.class));
    }

    @Test
    void loginAccountByEmail() {

        LoginRequestDTO dto = LoginRequestDTO.builder()
                .email("test@test.com")
                .password("test123")
                .build();

        Account account = Account.builder()
                .email("test@test.com")
                .password("encodePass")
                .build();

        LoginResponseDTO response = LoginResponseDTO.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

        when(accountRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.of(account));

        when(encoder.matches("test123", "encodePass"))
                .thenReturn(true);

        when(tokenService.generateTokens(
                eq(dto.getEmail()),
                eq(account),
                eq(account.getAuthorities())))
                .thenReturn(response);

        LoginResponseDTO result = accountService.loginAccountByEmail(dto);

        assertEquals("accessToken", result.getAccessToken());
        assertEquals("refreshToken", result.getRefreshToken());

        verify(tokenService).generateTokens(
                eq(dto.getEmail()),
                eq(account),
                any());
    }

    @Test
    void findAccountByID() {

        Account account = Account.builder()
                .id(1L)
                .email("Test@test.com")
                .name("Test name")
                .surname("Test surname")
                .hotels(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));

        AccountResponseDTO responseDTO = accountService.findAccountByID(1L);

        assertEquals("Test@test.com", responseDTO.getEmail());
        assertEquals("Test name", responseDTO.getName());
        assertEquals("Test surname", responseDTO.getSurname());
    }

    @Test
    void findAccountByIDAccountNotFound(){

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> accountService.findAccountByID(1L));

        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    void updateAccountByID() {

        AccountCreateDTO dto = AccountCreateDTO.builder()
                .email("Test@test.com")
                .name("Test name")
                .surname("Test surname")
                .password("test123")
                .repeatPassword("test123")
                .isHotelOwner(false)
                .build();

        Account account = Account.builder()
                .id(1L)
                .email(dto.getEmail())
                .name(dto.getName())
                .surname(dto.getSurname())
                .hotels(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();

        AccountUpdateDTO updateDTO = AccountUpdateDTO.builder()
                .email("test123@test.com")
                .name("New name")
                .surname("new surname")
                .build();

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AccountResponseDTO responseDTO = accountService.updateAccountByID(1L, updateDTO);

        assertEquals("test123@test.com", responseDTO.getEmail());
        assertEquals("New name", responseDTO.getName());
        assertEquals("new surname", responseDTO.getSurname());
    }

    @Test
    void updateAccountByIDAccountNotFound() {

        AccountUpdateDTO updateDTO = AccountUpdateDTO.builder()
                .email("test123@test.com")
                .name("New name")
                .surname("new surname")
                .build();

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> accountService.updateAccountByID(1L, updateDTO));

        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    void changePassword() {

        Account account = Account.builder()
                .id(1L)
                .email("Test@test.com")
                .name("Test name")
                .surname("Test surname")
                .password("oldEncodedPassword")
                .hotels(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();

        AccountChangePasswordDTO changePasswordDTO = AccountChangePasswordDTO.builder()
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .repeatNewPassword("newPassword")
                .build();

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));

        when(encoder.matches(
                changePasswordDTO.getOldPassword(),
                account.getPassword()
        )).thenReturn(true);

        when(encoder.encode(changePasswordDTO.getNewPassword()))
                .thenReturn("newEncodedPassword");

        when(accountRepository.save(any(Account.class)))
                .thenReturn(account);

        accountService.changePassword(1L, changePasswordDTO);

        assertEquals("newEncodedPassword", account.getPassword());

        verify(accountRepository)
                .save(account);

        verify(encoder)
                .encode("newPassword");
    }

    @Test
    void changePasswordAccountNotFound() {

        AccountChangePasswordDTO changePasswordDTO = AccountChangePasswordDTO.builder()
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .repeatNewPassword("newPassword")
                .build();

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> accountService.changePassword(1L, changePasswordDTO));

        assertEquals("Account not found", exception.getMessage());

        verify(accountRepository, never())
                .save(any(Account.class));
    }

    @Test
    void changePasswordOldPassNotMatch() {

        Account account = Account.builder()
                .id(1L)
                .email("Test@test.com")
                .name("Test name")
                .surname("Test surname")
                .password("oldEncodedPassword")
                .hotels(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();

        AccountChangePasswordDTO changePasswordDTO = AccountChangePasswordDTO.builder()
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .repeatNewPassword("newPassword")
                .build();

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));

        when(encoder.matches(
                changePasswordDTO.getOldPassword(),
                account.getPassword()
        )).thenReturn(false);

        PasswordNotMatchException exception = assertThrows(
                PasswordNotMatchException.class,
                () -> accountService.changePassword(1L, changePasswordDTO));

        assertEquals("The old password do not match", exception.getMessage());

        verify(accountRepository, never())
                .save(any(Account.class));
    }

    @Test
    void changePasswordNewPassNotMatch() {

        Account account = Account.builder()
                .id(1L)
                .email("Test@test.com")
                .name("Test name")
                .surname("Test surname")
                .password("oldEncodedPassword")
                .hotels(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();

        AccountChangePasswordDTO changePasswordDTO = AccountChangePasswordDTO.builder()
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .repeatNewPassword("newPassword123")
                .build();

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));

        when(encoder.matches(
                changePasswordDTO.getOldPassword(),
                account.getPassword()
        )).thenReturn(true);

        PasswordNotMatchException exception = assertThrows(
                PasswordNotMatchException.class,
                () -> accountService.changePassword(1L, changePasswordDTO));

        assertEquals("The new passwords do not match", exception.getMessage());

        verify(accountRepository, never())
                .save(any(Account.class));
    }

    @Test
    void deleteAccountByID() {

        Account account = Account.builder()
                .id(1L)
                .email("Test@test.com")
                .name("Test name")
                .surname("Test surname")
                .password("oldEncodedPassword")
                .hotels(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));

        accountService.deleteAccountByID(1L);

        verify(accountRepository).deleteById(account.getId());
    }

    @Test
    void deleteAccountByIDAccountNotFound() {

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> accountService.deleteAccountByID(1L));

        assertEquals("Account not find", exception.getMessage());

        verify(accountRepository, never())
                .deleteById(anyLong());
    }
}