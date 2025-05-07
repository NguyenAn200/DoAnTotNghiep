package com.hanoigarment.payroll.service;

import com.google.common.base.Optional;
import com.hanoigarment.payroll.dto.PositionDTO;
import java.util.List;

public interface PositionService {
    List<PositionDTO> getAllPositions();
    Optional<PositionDTO> getPositionById(Integer id);
    PositionDTO createPosition(PositionDTO positionDTO);
    PositionDTO updatePosition(Integer id, PositionDTO positionDTO);
    void deletePosition(Integer id);
}