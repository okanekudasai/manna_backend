package com.example.manna.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

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
}
