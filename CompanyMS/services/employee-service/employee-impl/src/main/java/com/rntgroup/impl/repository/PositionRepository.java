package com.rntgroup.impl.repository;

import com.rntgroup.impl.entity.Position;
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
