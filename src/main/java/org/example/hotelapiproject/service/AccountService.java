package org.example.hotelapiproject.service;

import org.example.hotelapiproject.dto.account_dto.AccountChangePasswordDTO;
import org.example.hotelapiproject.dto.account_dto.AccountCreateDTO;
import org.example.hotelapiproject.dto.account_dto.AccountUpdateDTO;
import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.entity.Role;
import org.example.hotelapiproject.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public Account registration(AccountCreateDTO dto) {

        if (!dto.getPassword().equals(dto.getRepeatPassword())) {
            throw new RuntimeException("The passwords do not match");
        }

        Account account = new Account();

        account.setName(dto.getName());
        if (Objects.nonNull(dto.getSurname())) {
            account.setSurname(dto.getSurname());
        }
        account.setEmail(dto.getEmail());
        account.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        if (dto.isHotelOwner()) {
            account.setRoles(Set.of(Role.ROLE_HOTEL_OWNER));
        } else {
            account.setRoles(Set.of(Role.ROLE_TRAVELER));
        }

        return accountRepository.save(account);
    }

    public Account findAccountByID(Long account_id) {
        return accountRepository.getReferenceById(account_id);
    }

    public Account updateAccountByID(Long account_id, AccountUpdateDTO dto) {
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

        return accountRepository.save(updateAccount);
    }

    public Account changePassword(Long account_id, AccountChangePasswordDTO dto) {
        Account updatePasswordAccount = accountRepository.getReferenceById(account_id);
        String encodeNewPassword = new BCryptPasswordEncoder().encode(dto.getNewPassword());

        if (!updatePasswordAccount.getPassword().equals(encodeNewPassword)) {
            throw new RuntimeException("The old password do not match");
        }

        if (!dto.getNewPassword().equals(dto.getRepeatNewPassword())) {
            throw new RuntimeException("The new passwords do not match");
        }

        updatePasswordAccount.setPassword(encodeNewPassword);

        return accountRepository.save(updatePasswordAccount);
    }

    public void deleteAccountByID(Long account_id) {
        accountRepository.deleteById(account_id);
    }
}
