package com.rntgroup.database.repository;

import com.rntgroup.database.entity.Ruler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RulerRepository extends JpaRepository<Ruler, Integer> {
    List<Ruler> findAllWhoManagedAtLeast(@Param("numberOfCities") Integer numberOfCities);

    @Query(value = """
            select r
            from Ruler r
            join r.rulerTerritorialUnits rtu
            join rtu.territorialUnit tu
            join tu.territoryType tt
            join tu.country c
            where lower(trim(tt.name)) = 'город' or lower(trim(tt.name)) = 'city'
            group by r, c
            having count(distinct tu) = (select count(distinct tu2)
                                         from TerritorialUnit tu2
                                         join tu2.territoryType tt2
                                         join tu2.country c2
                                         where (lower(trim(tt2.name)) = 'город' or lower(trim(tt2.name)) = 'city')
                                           and c2 = c)
            order by r.lastName, r.firstName, r.patronymic
            """)
    List<Ruler> findAllWhoRuledInAllCitiesOfAnyCountry();

    @Query(value = """
            SELECT r1.*
            FROM ruler r1
            JOIN ruler_territorial_unit rtu1
              ON r1.id = rtu1.ruler_id
            JOIN territorial_unit tu1
              ON rtu1.territorial_unit_id = tu1.id
            WHERE tu1.id IN (SELECT tu2.id
                             FROM ruler r2
                             JOIN ruler_territorial_unit rtu2
                               ON r2.id = rtu2.ruler_id
                             JOIN territorial_unit tu2
                               ON rtu2.territorial_unit_id = tu2.id
                             JOIN territory_type tt2
                               ON tu2.territory_type_id = tt2.id
                             WHERE (LOWER(TRIM(tt2.name)) = 'город' OR LOWER(TRIM(tt2.name)) = 'city')
                               AND tu2.name = :cityName
                               AND rtu1.ruler_id <> rtu2.ruler_id
                               AND rtu1.end_date <= rtu2.start_date
                               AND r2.id = :rulerId)
            ORDER BY rtu1.start_date DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<Ruler> findPreviousRulerOfTheCity(@Param("rulerId") Integer rulerId, @Param("cityName") String cityName);

}
