package com.hanoigarment.payroll.service.Imp;

import com.hanoigarment.payroll.dto.PageResponse;
import com.hanoigarment.payroll.dto.UserDTO;
import com.hanoigarment.payroll.dto.UserRequestDTO;
import com.hanoigarment.payroll.entity.User;
import com.hanoigarment.payroll.exception.ResourceNotFoundException;
import com.hanoigarment.payroll.repository.UserRepository;
import com.hanoigarment.payroll.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private UserDTO mapToDTO(User u) {
        UserDTO dto = new UserDTO();
        dto.setUserId(u.getUserId());
        dto.setUsername(u.getUsername());
        dto.setFullName(u.getFullName());
        dto.setEmail(u.getEmail());
        dto.setRole(u.getRole());
        dto.setIsActive(u.getIsActive());
        dto.setLastLogin(u.getLastLogin());
        return dto;
    }

    @Override
    public UserDTO createUser(UserRequestDTO req) {
        userRepository.findByUsername(req.getUsername()).ifPresent(u -> {
            throw new IllegalStateException("Username already exists");
        });
        userRepository.findByEmail(req.getEmail()).ifPresent(u -> {
            throw new IllegalStateException("Email already exists");
        });
        User u = new User();
        u.setUsername(req.getUsername());
        u.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        u.setFullName(req.getFullName());
        u.setEmail(req.getEmail());
        u.setRole(req.getRole());
        u.setIsActive(req.getIsActive() != null ? req.getIsActive() : true);
        User saved = userRepository.save(u);
        return mapToDTO(saved);
    }

    @Override
    public UserDTO getUserById(Integer id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return mapToDTO(u);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return mapToDTO(u);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return mapToDTO(u);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<UserDTO> getAllUsersPaged(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pg = PageRequest.of(pageNo, pageSize, sort);
        Page<User> page = userRepository.findAll(pg);
        List<UserDTO> content = page.getContent().stream().map(this::mapToDTO).collect(Collectors.toList());
        PageResponse<UserDTO> resp = new PageResponse<>();
        resp.setContent(content);
        resp.setPageNo(page.getNumber());
        resp.setPageSize(page.getSize());
        resp.setTotalElements(page.getTotalElements());
        resp.setTotalPages(page.getTotalPages());
        resp.setLast(page.isLast());
        return resp;
    }

    @Override
    public List<UserDTO> getUsersByRole(String role) {
        return userRepository.findByRole(role).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getActiveUsers() {
        return userRepository.findByIsActive(true).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Integer id, UserRequestDTO req) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        if (req.getFullName() != null) u.setFullName(req.getFullName());
        if (req.getEmail() != null) u.setEmail(req.getEmail());
        if (req.getRole() != null) u.setRole(req.getRole());
        if (req.getIsActive() != null) u.setIsActive(req.getIsActive());
        return mapToDTO(userRepository.save(u));
    }

    @Override
    public UserDTO updateUserLastLogin(Integer id, LocalDateTime lastLogin) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        u.setLastLogin(lastLogin);
        return mapToDTO(userRepository.save(u));
    }

    @Override
    public UserDTO changePassword(Integer id, String oldPassword, String newPassword) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        if (!passwordEncoder.matches(oldPassword, u.getPasswordHash())) {
            throw new IllegalStateException("Old password is incorrect");
        }
        u.setPasswordHash(passwordEncoder.encode(newPassword));
        return mapToDTO(userRepository.save(u));
    }

    @Override
    public UserDTO activateUser(Integer id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        u.setIsActive(true);
        return mapToDTO(userRepository.save(u));
    }

    @Override
    public UserDTO deactivateUser(Integer id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        u.setIsActive(false);
        return mapToDTO(userRepository.save(u));
    }

    @Override
    public void deleteUser(Integer id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(u);
    }
}
