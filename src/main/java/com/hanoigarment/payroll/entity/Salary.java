package com.hanoigarment.payroll.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "salaries",
        uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "period_month", "period_year"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salary_id")
    private Integer salaryId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "period_month", nullable = false)
    private Integer periodMonth;

    @Column(name = "period_year", nullable = false)
    private Integer periodYear;

    @Column(name = "base_salary", precision = 15, scale = 2)
    private BigDecimal baseSalary;

    @Column(name = "total_working_days", precision = 5, scale = 2)
    private BigDecimal totalWorkingDays;

    @Column(name = "actual_working_days", precision = 5, scale = 2)
    private BigDecimal actualWorkingDays;

    @Column(name = "overtime_amount", precision = 15, scale = 2)
    private BigDecimal overtimeAmount = BigDecimal.ZERO;

    private BigDecimal bonus = BigDecimal.ZERO;
    private BigDecimal allowance = BigDecimal.ZERO;

    @Column(name = "gross_salary", precision = 15, scale = 2)
    private BigDecimal grossSalary;

    @Column(name = "insurance_amount", precision = 15, scale = 2)
    private BigDecimal insuranceAmount = BigDecimal.ZERO;

    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "advance_amount", precision = 15, scale = 2)
    private BigDecimal advanceAmount = BigDecimal.ZERO;

    private BigDecimal deductions = BigDecimal.ZERO;

    @Column(name = "net_salary", precision = 15, scale = 2)
    private BigDecimal netSalary;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "payment_status")
    private String paymentStatus;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "salary", cascade = CascadeType.ALL)
    private List<SalaryDetail> salaryDetails;
}
