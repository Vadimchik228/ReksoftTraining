package com.rntgroup.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ruler_territorial_unit")
public class RulerTerritorialUnit implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ruler_id")
    private Ruler ruler;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "territorial_unit_id")
    private TerritorialUnit territorialUnit;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    private Integer century;

    public void setRuler(Ruler ruler) {
        this.ruler = ruler;
        this.ruler.getRulerTerritorialUnits().add(this);
    }

    public void setTerritorialUnit(TerritorialUnit territorialUnit) {
        this.territorialUnit = territorialUnit;
        this.territorialUnit.getRulerTerritorialUnits().add(this);
    }
}
