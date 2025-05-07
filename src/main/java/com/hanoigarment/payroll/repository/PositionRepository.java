package com.hanoigarment.payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.Position;
import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {
    List<Position> findByDepartmentDepartmentId(Integer departmentId);
}
