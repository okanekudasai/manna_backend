package com.example.manna.repository;

import com.example.manna.entity.Test;
import com.example.manna.entity.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoogleAuthRepository extends JpaRepository<UserDto, Long> {
    boolean existsBySerialNumber(String serial_number);
}
