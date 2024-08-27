package com.rntgroup.database.repository;

import com.rntgroup.database.entity.Ruler;

import java.util.List;

public interface CustomRulerRepository {
    List<Ruler> findAllWhoRuledInTheLargestNumberOfTerritorialUnits();
}
