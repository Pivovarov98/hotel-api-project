package org.example.hotelapiproject.repository;

import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.entity.RefreshToken;
import org.example.hotelapiproject.entity.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void deleteByAccountId() {

        Account account = mockAccount();
        entityManager.persist(account);

        entityManager.persist(mockToken(account));

        entityManager.flush();

        refreshTokenRepository.deleteByAccountId(account.getId());

        entityManager.flush();

        assertEquals(0, refreshTokenRepository.count());
    }

    @Test
    void deleteByToken() {

        Account account = mockAccount();
        entityManager.persist(account);

        entityManager.persist(mockToken(account));

        entityManager.flush();

        refreshTokenRepository.deleteByToken("actual");

        entityManager.flush();

        assertEquals(0, refreshTokenRepository.count());
    }

    @Test
    void deleteByExpiryDateBefore() {

        Account account1 = mockAccount();
        Account account2 = mockAccount();

        entityManager.persist(account1);
        entityManager.persist(account2);

        LocalDateTime now = LocalDateTime.now();

        RefreshToken expired = mockExpiredToken(account1);
        RefreshToken actual = mockToken(account2);

        entityManager.persist(expired);
        entityManager.persist(actual);

        entityManager.flush();

        refreshTokenRepository.deleteByExpiryDateBefore(now);

        entityManager.flush();

        List<RefreshToken> tokens = refreshTokenRepository.findAll();

        assertEquals(1, tokens.size());
        assertEquals("actual", tokens.getFirst().getToken());
    }

    private static Account mockAccount() {
        return Account.builder()
                .email("traveler@test.com")
                .name("Egor")
                .surname("Pivo")
                .password("test123")
                .roles(Set.of(Role.ROLE_TRAVELER))
                .build();
    }

    private static RefreshToken mockToken(Account account) {
        return RefreshToken.builder().token("actual")
                .account(account)
                .expiryDate(LocalDateTime.now().plusDays(1))
                .build();
    }

    private static RefreshToken mockExpiredToken(Account account) {
        return RefreshToken.builder().token("expired")
                .account(account)
                .expiryDate(LocalDateTime.now().minusDays(1))
                .build();
    }
}