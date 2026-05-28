package org.example.hotelapiproject.service;

import org.example.hotelapiproject.dto.auth_dto.AuthResponseDTO;
import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.entity.RefreshToken;
import org.example.hotelapiproject.repository.AccountRepository;
import org.example.hotelapiproject.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TokenService {
    @Value("${jwt.access.expiration:600000}")
    private long accessExpiration;

    @Value("${jwt.refresh.expiration:604800000}")
    private long refreshExpiration;

    private final String SECRET_KEY = "supersecretkeyforjwt";

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ApplicationContext applicationContext;

    private SecretKey generateKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateAccessToken(String email, Long accountId, List<String> roles) {

        return Jwts.builder()
                .setClaims(Map.of("roles", roles))
                .setSubject(email)
                .claim("accountId", accountId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String email, Long accountId, List<String> roles) {

        return Jwts.builder()
                .setSubject(email)
                .claim("accountId", accountId)
                .claim("roles", roles)
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(generateKey())
                    .build();
            return parser.parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new InternalAuthenticationServiceException("Token expired");
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException("Invalid token");
        }
    }

    public AuthResponseDTO generateTokens(String email, Account account,
                                          Collection<? extends GrantedAuthority> authorities) {
        refreshTokenRepository.deleteByAccountId(account.getId());

        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String accessToken = generateAccessToken(email, account.getId(), roles);
        String refreshToken = generateRefreshToken(email, account.getId(), roles);

        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setAccount(account);
        refreshTokenEntity.setExpiryDate(LocalDateTime.now().plusSeconds(refreshExpiration / 1000));

        refreshTokenRepository.save(refreshTokenEntity);

        return new AuthResponseDTO(accessToken, refreshToken);
    }
}