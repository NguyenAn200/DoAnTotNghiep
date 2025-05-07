package com.hanoigarment.payroll.repository;

import com.hanoigarment.payroll.entity.SalaryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryDetailRepository extends JpaRepository<SalaryDetail, Integer> {
  List<SalaryDetail> findBySalarySalaryId(Integer salaryId);
}