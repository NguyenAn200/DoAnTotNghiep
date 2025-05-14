package com.hanoigarment.payroll.service;

import com.google.common.base.Optional;
import com.hanoigarment.payroll.dto.PageResponse;
import com.hanoigarment.payroll.dto.PositionDTO;
import com.hanoigarment.payroll.dto.PositionRequestDTO;

import java.util.List;

public interface PositionService {
    PositionDTO createPosition(PositionRequestDTO requestDTO);

    PositionDTO getPositionById(Integer positionId);

    PositionDTO getPositionByIdWithEmployees(Integer positionId);

    PositionDTO getPositionByName(String positionName);

    List<PositionDTO> getPositionsByActiveFlag(Boolean isActive);

    List<PositionDTO> getAllPositions();

    List<PositionDTO> getAllActivePositions();

    PageResponse<PositionDTO> getAllPositionsPaged(int pageNo, int pageSize, String sortBy, String sortDir);

    List<PositionDTO> getPositionsByDepartmentId(Integer departmentId);

    List<PositionDTO> getActivePositionsByDepartmentId(Integer departmentId);

    PositionDTO updatePosition(Integer positionId, PositionRequestDTO requestDTO);

    PositionDTO activatePosition(Integer positionId);

    PositionDTO deactivatePosition(Integer positionId);

    void deletePosition(Integer positionId);
}