package com.example.manna.repository;

import com.example.manna.entity.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDto, Long> {
    boolean existsBySerialNumber(String serial_number);

    UserDto findBySerialNumber(String serial_number);
}
