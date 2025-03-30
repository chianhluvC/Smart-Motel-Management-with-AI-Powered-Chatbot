package com.dacn.Nhom8QLPhongTro.services;


import com.dacn.Nhom8QLPhongTro.entity.RefreshToken;
import com.dacn.Nhom8QLPhongTro.exceptions.TokenRefreshException;
import com.dacn.Nhom8QLPhongTro.repository.IRefreshTokenRepository;
import com.dacn.Nhom8QLPhongTro.repository.IuserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;




@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final IRefreshTokenRepository refreshTokenRepository;
    private final IuserRepository userRepository;

    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = RefreshToken.builder().user(userRepository.findByUsername(username))
                .token(UUID.randomUUID().toString()).expiryDate(Instant.now()
                        .plusMillis(1000*60*60*24)).build(); // Refresh Token valid for 24 hours
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyRefreshToken(RefreshToken token) {
        if(token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }


    public void deleteRefreshToken(RefreshToken token) {
        refreshTokenRepository.delete(token);
    }




}
