package org.example.hotelapiproject.service;

import org.example.hotelapiproject.dto.account_dto.AccountChangePasswordDTO;
import org.example.hotelapiproject.dto.account_dto.AccountCreateDTO;
import org.example.hotelapiproject.dto.account_dto.AccountResponseDTO;
import org.example.hotelapiproject.dto.account_dto.AccountUpdateDTO;
import org.example.hotelapiproject.dto.auth_dto.LoginRequestDTO;
import org.example.hotelapiproject.dto.auth_dto.LoginResponseDTO;
import org.example.hotelapiproject.dto.hotel_dto.HotelShortDTO;
import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.entity.Hotel;
import org.example.hotelapiproject.entity.enums.Role;
import org.example.hotelapiproject.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TokenService tokenService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AccountResponseDTO registration(AccountCreateDTO dto) {

        if (!dto.getPassword().equals(dto.getRepeatPassword())) {
            throw new RuntimeException("The passwords do not match");
        }

        Account account = new Account();

        account.setName(dto.getName());
        if (Objects.nonNull(dto.getSurname())) {
            account.setSurname(dto.getSurname());
        }
        account.setEmail(dto.getEmail());
        account.setPassword(encoder.encode(dto.getPassword()));
        if (dto.isHotelOwner()) {
            account.setRoles(Set.of(Role.ROLE_HOTEL_OWNER));
        } else {
            account.setRoles(Set.of(Role.ROLE_TRAVELER));
        }

        return responseDTO( accountRepository.save(account));
    }

    public LoginResponseDTO loginAccountByEmail(LoginRequestDTO dto) throws UsernameNotFoundException {
        Optional<Account> optionAccount = accountRepository.findByEmail(dto.getEmail());

        if (!optionAccount.isPresent()) {
            throw new UsernameNotFoundException("Account not found");
        }

        Account account = optionAccount.get();
        boolean matches = encoder.matches(dto.getPassword(), account.getPassword());

        if (!matches) {
            throw new UsernameNotFoundException("Incorrect password");
        }

        return tokenService.generateTokens(dto.getEmail(), account, account.getAuthorities());
    }

    public AccountResponseDTO findAccountByID(Long account_id) {
        return responseDTO(accountRepository.getReferenceById(account_id));
    }

    public AccountResponseDTO updateAccountByID(Long account_id, AccountUpdateDTO dto) {
        Account updateAccount = accountRepository.getReferenceById(account_id);

        if (Objects.nonNull(dto.getEmail())) {
            updateAccount.setEmail(dto.getEmail());
        }

        if (Objects.nonNull(dto.getName())) {
            updateAccount.setName(dto.getName());
        }

        if (Objects.nonNull(dto.getSurname())) {
            updateAccount.setSurname(dto.getSurname());
        }

        return responseDTO(accountRepository.save(updateAccount));
    }

    public AccountResponseDTO changePassword(Long account_id, AccountChangePasswordDTO dto) {
        Account account = accountRepository.getReferenceById(account_id);

        if (!encoder.matches(dto.getOldPassword(), account.getPassword())) {

            throw new RuntimeException("The old password do not match");
        }

        if (!dto.getNewPassword().equals(dto.getRepeatNewPassword())) {
            throw new RuntimeException("The new passwords do not match");
        }

        account.setPassword(encoder.encode(dto.getNewPassword()));

        return responseDTO(accountRepository.save(account));
    }

    public void deleteAccountByID(Long account_id) {

        Account account = accountRepository.findById(account_id).
                orElseThrow(() -> new RuntimeException("Account not find"));

        accountRepository.deleteById(account_id);
    }

    private AccountResponseDTO responseDTO (Account account){
        AccountResponseDTO dto = new AccountResponseDTO();

        dto.setEmail(account.getEmail());
        dto.setName(account.getName());
        dto.setSurname(account.getSurname());
        dto.setHotels(account.getHotels()
                .stream()
                .map(this::hotelToShortDTO)
                .toList());

        return dto;
    }

    private HotelShortDTO hotelToShortDTO(Hotel hotel){
        return new HotelShortDTO(hotel.getId(), hotel.getName());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> byUsername = accountRepository.findByEmail(email);

        if (byUsername.isPresent()) {
            return byUsername.get();
        }

        throw new UsernameNotFoundException("User not found");
    }
}
