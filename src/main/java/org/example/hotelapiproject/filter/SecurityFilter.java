package org.example.hotelapiproject.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.entity.Role;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.*;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private SecretKey generateKey() {
        return Keys.hmacShaKeyFor("supersecretkeyforjwtsgdewthertyjregscedgwerfhwealkjfpiqwjhfdoigsefihguoisenbiewufhgowejfotg".getBytes());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String PREFIX_TOKEN = "Bearer ";
        String header = request.getHeader("Authorization");;

        if ( Objects.nonNull(header) && header.startsWith(PREFIX_TOKEN)) {
            String   token = header.substring(PREFIX_TOKEN.length());

            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(generateKey())
                    .build();

            try {
                Claims body = parser.parseClaimsJws(token).getBody();


                List roles = body.get("roles", List.class);
                Long accountId = body.get("accountId", Long.class);

                Account account = new Account();
                account.setId(accountId);
                account.setEmail(body.getSubject());

                Set<Role> roleSet = new HashSet<>();

                for (Object roleObj : roles) {
                    String roleName = (String) roleObj;
                    roleSet.add(Role.valueOf(roleName));
                }

                account.setRoles(roleSet);

                Collection<? extends GrantedAuthority> authorities = account.getAuthorities();

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(account, token, authorities);


                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            } catch (ExpiredJwtException e) {
                throw new InternalAuthenticationServiceException("Invalid JWT: " + e.getMessage());
            }

        }

        filterChain.doFilter(request, response);
    }
}