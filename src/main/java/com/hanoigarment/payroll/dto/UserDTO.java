package com.hanoigarment.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer userId;
    private String username;
    private String fullName;
    private String email;
    private String role;
    private Boolean isActive;
    private LocalDateTime lastLogin;
}

