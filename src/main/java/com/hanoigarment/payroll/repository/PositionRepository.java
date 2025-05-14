package com.hanoigarment.payroll.repository;

import com.hanoigarment.payroll.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {

    Optional<Position> findByPositionName(String positionName);

    List<Position> findByDepartmentDepartmentId(Integer departmentId);

    List<Position> findByIsActive(Boolean isActive);

    @Query("SELECT p FROM Position p WHERE p.isActive = true ORDER BY p.positionName")
    List<Position> findAllActivePositions();

    @Query("SELECT p FROM Position p JOIN FETCH p.employees WHERE p.positionId = :positionId")
    Optional<Position> findByIdWithEmployees(Integer positionId);

    @Query("SELECT p FROM Position p WHERE p.department.departmentId = :departmentId AND p.isActive = true")
    List<Position> findActivePositionsByDepartmentId(Integer departmentId);
}