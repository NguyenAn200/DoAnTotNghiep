package com.hanoigarment.payroll.service;

import com.hanoigarment.payroll.dto.UserDTO;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> getAllUsers();
    Optional<UserDTO> getUserById(Integer id);
    Optional<UserDTO> getByUsername(String username);
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(Integer id, UserDTO userDTO);
    void deleteUser(Integer id);
}