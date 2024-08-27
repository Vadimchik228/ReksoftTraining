package com.rntgroup.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString(exclude = "rulerTerritorialUnits")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "territorial_unit")
public class TerritorialUnit implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "territory_type_id")
    private TerritoryType territoryType;

    @Column(nullable = false)
    private LocalDate foundationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(nullable = false)
    private Integer population;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "territorialUnit")
    private List<RulerTerritorialUnit> rulerTerritorialUnits = new ArrayList<>();
}
