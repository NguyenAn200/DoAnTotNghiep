package com.hanoigarment.payroll.repository;

import com.google.common.base.Optional;
import com.hanoigarment.payroll.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    Optional<Department> findByDepartmentName(String departmentName);

    List<Department> findByIsActive(Boolean isActive);

    @Query("SELECT d FROM Department d WHERE d.isActive = true ORDER BY d.departmentName")
    List<Department> findAllActiveDepartments();

    @Query("SELECT d FROM Department d JOIN FETCH d.positions WHERE d.departmentId = :departmentId")
    Optional<Department> findByIdWithPositions(Integer departmentId);

    @Query("SELECT d FROM Department d JOIN FETCH d.employees WHERE d.departmentId = :departmentId")
    Optional<Department> findByIdWithEmployees(Integer departmentId);
}
