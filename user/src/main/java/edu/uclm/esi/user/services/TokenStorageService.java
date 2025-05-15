package edu.uclm.esi.user.services;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class TokenStorageService {
    private static class TokenInfo {
        private final String email;
        private final LocalDateTime expiration;

        public TokenInfo(String email, LocalDateTime expiration) {
            this.email = email;
            this.expiration = expiration;
        }

        public String getEmail() {
            return email;
        }

        public LocalDateTime getExpiration() {
            return expiration;
        }
    }

    private final ConcurrentHashMap<String, TokenInfo> tokenStore = new ConcurrentHashMap<>();

    public void saveToken(String token, String email, LocalDateTime expiration) {
        tokenStore.put(token, new TokenInfo(email, expiration));
    }

    public boolean isTokenValid(String token, String email) {
        TokenInfo tokenInfo = tokenStore.get(token);
        System.out.println("Token: " + token + ", Email: " + email);
        System.out.println("TokenInfo: " + tokenInfo);
        
        return tokenInfo != null && LocalDateTime.now().isBefore(tokenInfo.getExpiration())&&email.equals(tokenInfo.getEmail());
    }

    public void removeToken(String token) {
        tokenStore.remove(token);
    }

    
    public void printTokens() {
        tokenStore.forEach((token, info) -> {
            System.out.println("Token: " + token + ", Email: " + info.getEmail() + ", Expiration: " + info.getExpiration());
        });
    }
}
