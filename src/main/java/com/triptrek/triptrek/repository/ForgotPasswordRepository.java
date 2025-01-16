package com.triptrek.triptrek.repository;

import com.triptrek.triptrek.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByToken(String token);
}
