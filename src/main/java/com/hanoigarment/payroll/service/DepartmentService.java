package com.hanoigarment.payroll.service;

import com.hanoigarment.payroll.dto.DepartmentDTO;
import com.hanoigarment.payroll.entity.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    List<DepartmentDTO> getAllDepartments();
    Optional<DepartmentDTO> getDepartmentById(Integer id);
    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);
    DepartmentDTO updateDepartment(Integer id, DepartmentDTO departmentDTO);
    void deleteDepartment(Integer id);
}