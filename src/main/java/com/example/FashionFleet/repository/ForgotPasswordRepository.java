package com.example.FashionFleet.repository;

import com.example.FashionFleet.domain.ForgotPassword;
import com.example.FashionFleet.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword,Integer> {
    @Query("SELECT fp FROM ForgotPassword fp JOIN fp.user u WHERE fp.otp = :otp AND u.email = :email")
    Optional<ForgotPassword> findByOtpAndUserEmail(@Param("otp") Integer otp, @Param("email") String email);

    @Query("select fp from ForgotPassword fp where fp.user.email = ?1")
    Optional<ForgotPassword> findByUserEmail(String email);

    ForgotPassword findByUser(User user);
}
