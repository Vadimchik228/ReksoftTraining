package com.rntgroup.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NamedQuery(
        name = "Ruler.findAllWhoManagedAtLeast",
        query = """
                select r
                from Ruler r
                join r.rulerTerritorialUnits rtu
                join rtu.territorialUnit tu
                join tu.territoryType tt
                where lower(trim(tt.name)) = 'город' or lower(trim(tt.name)) = 'city'
                group by r
                having count(distinct tu) >= :numberOfCities
                order by r.lastName, r.firstName, r.patronymic
                """
)
@Data
@ToString(exclude = "rulerTerritorialUnits")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ruler")
public class Ruler implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    private String patronymic;

    private String nickname;

    private LocalDate birthDate;

    private LocalDate deathDate;

    @Column(nullable = false)
    private Integer century;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "ruler")
    private List<RulerTerritorialUnit> rulerTerritorialUnits = new ArrayList<>();
}