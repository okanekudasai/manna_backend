package com.example.manna.entity.user;

import com.example.manna.entity.feed.FeedDto;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname")
    private String name;

    @Column(name = "profile_url")
    private String profile_url;

    @Column(name = "age")
    private Integer age;

    @Column(name = "sex")
    private String sex;

    @Column(name = "city")
    private String city;

    @Column(name = "nation")
    private String nation;

    @Column(name = "created_at")
    private LocalDateTime createdDate;

    @Column(name = "deleted")
    private Boolean deleted;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy="writer")
    private List<FeedDto> feed_list = new ArrayList<>();
}
