package com.hanoigarment.payroll.utils;

import com.hanoigarment.payroll.config.HostConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

public class TokenJwtUtil {

    private static final long EXPIRATION_TIME = 86_400_000; // 1 day in ms

    /**
     * Tạo JWT token với key mã hóa HS256
     */
    public static String generateToken(String username, String role, HostConfig config) {
        Key key = Keys.hmacShaKeyFor(config.getSecretKey().getBytes());
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    /**
     * Lấy Authentication từ request nếu token hợp lệ
     */
    public static Authentication getAuthentication(HttpServletRequest request, HostConfig config) {
        String token = resolveToken(request);
        if (token != null && validateToken(token, config)) {
            Claims claims = getClaims(token, config);
            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            return new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority(role))
            );
        }
        return null;
    }

    /**
     * Validate JWT token
     */
    public static boolean validateToken(String token, HostConfig config) {
        try {
            getClaims(token, config);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Phân tích và trả về Claims từ token
     * Sử dụng API cũ parser() để tương thích JJWT 0.12.3
     */
    private static Claims getClaims(String token, HostConfig config) {
        Key key = Keys.hmacShaKeyFor(config.getSecretKey().getBytes());
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    /**
     * Lấy token từ header Authorization: Bearer ...
     */
    private static String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
