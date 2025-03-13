package com.triptrek.triptrek.repository;

import com.triptrek.triptrek.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);


}
