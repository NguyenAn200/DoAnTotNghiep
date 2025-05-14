package com.hanoigarment.payroll.repository;

import com.hanoigarment.payroll.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByEmployeeCode(String employeeCode);

    Optional<Employee> findByIdCard(String idCard);

    List<Employee> findByDepartmentDepartmentId(Integer departmentId);

    List<Employee> findByPositionPositionId(Integer positionId);

    List<Employee> findByIsActive(Boolean isActive);

    @Query("SELECT e FROM Employee e WHERE e.isActive = true ORDER BY e.fullName")
    List<Employee> findAllActiveEmployees();

    @Query("SELECT e FROM Employee e WHERE LOWER(e.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Employee> findByFullNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT e FROM Employee e WHERE e.joinDate BETWEEN :startDate AND :endDate")
    List<Employee> findByJoinDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT e FROM Employee e JOIN FETCH e.attendances WHERE e.employeeId = :employeeId")
    Optional<Employee> findByIdWithAttendances(Integer employeeId);

    @Query("SELECT e FROM Employee e JOIN FETCH e.salaries WHERE e.employeeId = :employeeId")
    Optional<Employee> findByIdWithSalaries(Integer employeeId);

    @Query("SELECT e FROM Employee e JOIN FETCH e.advances WHERE e.employeeId = :employeeId")
    Optional<Employee> findByIdWithAdvances(Integer employeeId);
}
