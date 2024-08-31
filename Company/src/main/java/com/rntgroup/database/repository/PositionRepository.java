package com.rntgroup.database.repository;

import com.rntgroup.database.entity.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PositionRepository extends JpaRepository<Position, Integer> {
    @Query(value = """
            select p
            from Position p
            """)
    Page<Position> findAllBy(Pageable pageable);
}
