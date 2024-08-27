package com.rntgroup.database.repository;

import com.rntgroup.database.entity.Ruler;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomRulerRepository {

    private final EntityManager entityManager;

    /*
        -- Это как выглядел исходный запрос
        SELECT r.*
        FROM ruler r
        JOIN ruler_territorial_unit rtu ON r.id = rtu.ruler_id
        JOIN territorial_unit tu ON rtu.territorial_unit_id = tu.id
        GROUP BY r.id, r.last_name, r.first_name, r.patronymic
        HAVING COUNT(DISTINCT tu.id) = (
            SELECT MAX(ruled_territories)
            FROM (
                  SELECT COUNT(DISTINCT tu.id) AS ruled_territories
                  FROM ruler r
                  JOIN ruler_territorial_unit rtu ON r.id = rtu.ruler_id
                  JOIN territorial_unit tu ON rtu.territorial_unit_id = tu.id
                  GROUP BY r.id
                 ) AS subquery
        )
        ORDER BY r.last_name, r.first_name, r.patronymic;
     */
    @Override
    public List<Ruler> findAllWhoRuledInTheLargestNumberOfTerritorialUnits() {
        var cb = entityManager.getCriteriaBuilder();

        var query = cb.createQuery(Ruler.class);
        var rulerRoot = query.from(Ruler.class);

        var rulerTerritorialUnitJoin = rulerRoot.join("rulerTerritorialUnits");
        var territorialUnitJoin = rulerTerritorialUnitJoin.join("territorialUnit");

        query.groupBy(rulerRoot.get("id"), rulerRoot.get("lastName"), rulerRoot.get("firstName"), rulerRoot.get("patronymic"));

        query.having(cb.equal(cb.countDistinct(territorialUnitJoin.get("id")),
                getMaxValueOfSubquery()));

        query.orderBy(cb.asc(rulerRoot.get("lastName")),
                cb.asc(rulerRoot.get("firstName")),
                cb.asc(rulerRoot.get("patronymic")));

        return entityManager.createQuery(query).getResultList();
    }

    public Long getMaxValueOfSubquery() {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(Long.class);
        var rulerRoot = query.from(Ruler.class);

        var rulerTerritorialUnitJoin = rulerRoot.join("rulerTerritorialUnits");
        var territorialUnitJoin = rulerTerritorialUnitJoin.join("territorialUnit");

        query.groupBy(rulerRoot.get("id"));

        var countDistinct = cb.countDistinct(territorialUnitJoin.get("id"));
        query.select(countDistinct);

        query.orderBy(cb.desc(countDistinct));

        var typedQuery = entityManager.createQuery(query);
        typedQuery.setMaxResults(1);
        return typedQuery.getSingleResult();
    }

}
