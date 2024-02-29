package com.example.manna.repository;

import com.example.manna.entity.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDto, Long> {
    boolean existsBySerialNumberAndDeleted(String serial_number, boolean deleted);
    UserDto findBySerialNumberAndDeleted(String serial_number, boolean deleted);

    UserDto findBySerialNumber(String serial_number);
}
