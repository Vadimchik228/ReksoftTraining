package com.rntgroup.impl.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Generated
@Entity
public class DepartmentPayroll implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer departmentId;

    @Column(nullable = false)
    private BigDecimal salaryFund;

    @Column(nullable = false)
    private LocalDateTime timestamp;

}
