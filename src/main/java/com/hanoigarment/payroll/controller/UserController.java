package com.hanoigarment.payroll.controller;

import com.hanoigarment.payroll.dto.PageResponse;
import com.hanoigarment.payroll.dto.UserDTO;
import com.hanoigarment.payroll.dto.UserRequestDTO;
import com.hanoigarment.payroll.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users/v1.0")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
@Validated
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // Create user
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody UserRequestDTO req) {
        try {
            UserDTO dto = userService.createUser(req);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error creating user", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get by ID
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            UserDTO dto = userService.getUserById(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching user id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get by username
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/search/username", method = RequestMethod.GET)
    public ResponseEntity<?> getByUsername(@RequestParam String username) {
        try {
            UserDTO dto = userService.getUserByUsername(username);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching user by username {}", username, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get by email
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/search/email", method = RequestMethod.GET)
    public ResponseEntity<?> getByEmail(@RequestParam String email) {
        try {
            UserDTO dto = userService.getUserByEmail(email);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching user by email {}", email, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get all users
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        try {
            List<UserDTO> list = userService.getAllUsers();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching all users", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Paged users
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/paged", method = RequestMethod.GET)
    public ResponseEntity<?> getPaged(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "userId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            PageResponse<UserDTO> page = userService.getAllUsersPaged(pageNo, pageSize, sortBy, sortDir);
            return ResponseEntity.ok(page);
        } catch (Exception e) {
            logger.error("Error fetching paged users", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get by role
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/role/{role}", method = RequestMethod.GET)
    public ResponseEntity<?> getByRole(@PathVariable String role) {
        try {
            List<UserDTO> list = userService.getUsersByRole(role);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching users by role {}", role, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get active users
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public ResponseEntity<?> getActive() {
        try {
            List<UserDTO> list = userService.getActiveUsers();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching active users", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Update user
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody UserRequestDTO req) {
        try {
            UserDTO dto = userService.updateUser(id, req);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error updating user id {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Update last login
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}/last-login", method = RequestMethod.PUT)
    public ResponseEntity<?> updateLastLogin(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastLogin) {
        try {
            UserDTO dto = userService.updateUserLastLogin(id, lastLogin);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error updating last login for user id {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Change password
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}/password", method = RequestMethod.PUT)
    public ResponseEntity<?> changePassword(
            @PathVariable Integer id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        try {
            UserDTO dto = userService.changePassword(id, oldPassword, newPassword);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error changing password for user id {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Activate
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}/activate", method = RequestMethod.POST)
    public ResponseEntity<?> activate(@PathVariable Integer id) {
        try {
            UserDTO dto = userService.activateUser(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error activating user id {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Deactivate
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}/deactivate", method = RequestMethod.POST)
    public ResponseEntity<?> deactivate(@PathVariable Integer id) {
        try {
            UserDTO dto = userService.deactivateUser(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error deactivating user id {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Delete
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting user id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
