package com.triptrek.triptrek.service;

import com.triptrek.triptrek.model.User;
import com.triptrek.triptrek.repository.ForgotPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.time.Duration;


@Service
public class ForgotPasswordService {
    private static final long EXPIRE_TOKEN=1;

    @Autowired
    private ForgotPasswordRepository repository;

    public String forgotPassword(String email){
        Optional<User> userOptional= Optional.ofNullable(repository.findByEmail(email));

        if(!userOptional.isPresent()){
            return "Email not registered";
        }

        User user = userOptional.get();
        user.setToken(generateToken());
        user.setTokenCreationDate(LocalDateTime.now());
        repository.save(user);

        return user.getToken();
    }

    public String resetPassword(String token, String password) {
        Optional<User> userOptional = Optional.ofNullable(repository.findByToken(token));

        if (!userOptional.isPresent()) {
            return "Invalid link";
        }
        LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();

        if (isTokenExpired(tokenCreationDate)) {
            return "Expired link";
        }

        User user = userOptional.get();
        user.setPassword(password);
        user.setToken(null);
        user.setTokenCreationDate(null);
        repository.save(user);

        return "Your password is successfully changed";
    }


    private String generateToken(){
        StringBuilder token = new StringBuilder();

        return token.append(UUID.randomUUID().toString())
                .append(UUID.randomUUID().toString()).toString();
    }

    private boolean isTokenExpired(final LocalDateTime tokenCreationDate){

        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(tokenCreationDate, now);

        return diff.toHours() > EXPIRE_TOKEN;
    }




}
