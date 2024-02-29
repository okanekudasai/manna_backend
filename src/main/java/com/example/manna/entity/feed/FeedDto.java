package com.example.manna.entity.feed;

import com.example.manna.entity.user.UserDto;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long article_idx;

    @ManyToOne
    @JoinColumn(name = "writer_idx")
    private UserDto writer;

    @Column(name = "article_title")
    private String article_title;

    @Column(name = "article_content", length = 50000)
    private String article_content;

    @Column(name = "article_thumbnail")
    private String article_thumbnail;

    @Column(name = "heart_count")
    private Integer heart_count;

    @Column(name = "reply_count")
    private Integer reply_count;

    @Column(name = "created_date")
    private LocalDateTime createdTime;

    @Column(name = "deleted")
    private Boolean deleted;

}
