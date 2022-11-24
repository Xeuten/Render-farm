package com.example.renderFarmServer.persistence;

import com.example.renderFarmServer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);

    @Query(value = "SELECT CASE WHEN COUNT(user_id) > 0 THEN TRUE ELSE FALSE END FROM users WHERE username = :userName AND password = :password",
    nativeQuery = true)
    boolean userAndPasswordExist(@Param("userName") String userName, @Param("password") String password);
}
