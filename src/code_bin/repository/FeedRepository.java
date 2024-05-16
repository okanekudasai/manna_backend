package com.example.manna.repository;

import com.example.manna.entity.feed.FeedDto;
import com.example.manna.entity.user.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<FeedDto, Long> {
    Page<FeedDto> findAllByOrderByCreatedTimeDesc(Pageable pageable);
}
