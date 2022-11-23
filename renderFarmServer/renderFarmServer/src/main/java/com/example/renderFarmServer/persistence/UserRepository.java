package com.example.renderFarmServer.persistence;

import com.example.renderFarmServer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT CASE WHEN COUNT(user_id) > 0 THEN TRUE ELSE FALSE END FROM users WHERE user_id = :userID AND password = :password",
    nativeQuery = true)
    boolean userAndPasswordExist(@Param("userID") Long userId, @Param("password") String password);
}
