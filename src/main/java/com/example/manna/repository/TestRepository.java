package com.example.manna.repository;

import com.example.manna.entity.TestDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<TestDto, Long> {
}
