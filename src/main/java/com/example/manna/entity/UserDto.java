package com.example.manna.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class UserDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(name = "serial_number")
    private String serial_number;

    @Column(name = "created_date")
    private Date created_date;

    @Column(name = "deleted")
    private Boolean deleted;
}
