package com.hanoigarment.payroll.service;

import com.hanoigarment.payroll.dto.PageResponse;
import com.hanoigarment.payroll.dto.UserDTO;
import com.hanoigarment.payroll.dto.UserRequestDTO;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDTO createUser(UserRequestDTO requestDTO);

    UserDTO getUserById(Integer userId);

    UserDTO getUserByUsername(String username);

    UserDTO getUserByEmail(String email);

    List<UserDTO> getAllUsers();

    PageResponse<UserDTO> getAllUsersPaged(int pageNo, int pageSize, String sortBy, String sortDir);

    List<UserDTO> getUsersByRole(String role);

    List<UserDTO> getActiveUsers();

    UserDTO updateUser(Integer userId, UserRequestDTO requestDTO);

    UserDTO updateUserLastLogin(Integer userId, LocalDateTime lastLogin);

    UserDTO changePassword(Integer userId, String oldPassword, String newPassword);

    UserDTO activateUser(Integer userId);

    UserDTO deactivateUser(Integer userId);

    void deleteUser(Integer userId);
}