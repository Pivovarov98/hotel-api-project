package org.example.hotelapiproject.controller;

import org.example.hotelapiproject.dto.account_dto.AccountChangePasswordDTO;
import org.example.hotelapiproject.dto.account_dto.AccountCreateDTO;
import org.example.hotelapiproject.dto.account_dto.AccountUpdateDTO;
import org.example.hotelapiproject.dto.auth_dto.LoginRequestDTO;
import org.example.hotelapiproject.dto.auth_dto.LoginResponseDTO;
import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/registration")
    public ResponseEntity<Account> registration(@RequestBody AccountCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.registration(dto));
    }

    @PostMapping("/log_in")
    public ResponseEntity<LoginResponseDTO> auth(@RequestBody LoginRequestDTO dto){
        return ResponseEntity.ok(accountService.loginAccountByEmail(dto));
    }

    @GetMapping("/{account_id}")
    public Account findAccountByID(@PathVariable Long account_id) {
        return accountService.findAccountByID(account_id);
    }

    @PatchMapping("/{account_id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long account_id,
                                                 @RequestBody AccountUpdateDTO dto) {
        return ResponseEntity.ok(accountService.updateAccountByID(account_id, dto));
    }

    @PatchMapping("/change-password/{account_id}")
    public ResponseEntity<Account> changePassword(@PathVariable Long account_id,
                                                  @RequestBody AccountChangePasswordDTO dto) {
        return ResponseEntity.ok(accountService.changePassword(account_id, dto));
    }

    @DeleteMapping("/{account_id}")
    public ResponseEntity<Account> deleteAccountByID(@PathVariable Long account_id) {
        accountService.deleteAccountByID(account_id);
        return ResponseEntity.noContent().build();
    }
}
