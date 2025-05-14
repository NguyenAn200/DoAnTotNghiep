package com.hanoigarment.payroll.service;

import com.hanoigarment.payroll.dto.DepartmentDTO;
import com.hanoigarment.payroll.dto.DepartmentRequestDTO;
import com.hanoigarment.payroll.dto.PageResponse;
import com.hanoigarment.payroll.entity.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    DepartmentDTO createDepartment(DepartmentRequestDTO requestDTO);

    DepartmentDTO getDepartmentById(Integer departmentId);

    DepartmentDTO getDepartmentByIdWithPositions(Integer departmentId);

    DepartmentDTO getDepartmentByIdWithEmployees(Integer departmentId);

    DepartmentDTO getDepartmentByName(String departmentName);

    List<DepartmentDTO> getAllDepartments();

    List<DepartmentDTO> getAllActiveDepartments();

    PageResponse<DepartmentDTO> getAllDepartmentsPaged(int pageNo, int pageSize, String sortBy, String sortDir);

    DepartmentDTO updateDepartment(Integer departmentId, DepartmentRequestDTO requestDTO);

    DepartmentDTO activateDepartment(Integer departmentId);

    DepartmentDTO deactivateDepartment(Integer departmentId);

    void deleteDepartment(Integer departmentId);
}