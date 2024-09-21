package com.rntgroup.impl.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class DepartmentSnapshot implements BaseEntity<Integer> {

    @Id
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

}
