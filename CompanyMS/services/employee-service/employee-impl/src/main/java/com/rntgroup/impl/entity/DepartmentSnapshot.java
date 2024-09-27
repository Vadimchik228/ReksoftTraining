package com.rntgroup.impl.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Generated
@Entity
public class DepartmentSnapshot implements BaseEntity<Integer> {

    @Id
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

}
