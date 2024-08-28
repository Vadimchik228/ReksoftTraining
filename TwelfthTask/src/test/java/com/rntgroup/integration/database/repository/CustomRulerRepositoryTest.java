package com.rntgroup.integration.database.repository;

import com.rntgroup.database.repository.CustomRulerRepository;
import com.rntgroup.integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@IT
@Sql({
        "classpath:sql/data.sql"
})
@RequiredArgsConstructor
class CustomRulerRepositoryTest {

    private final CustomRulerRepository customRulerRepository;

    @Test
    void findAllWhoRuledInTheLargestNumberOfTerritorialUnits() {
        var rulers = customRulerRepository.findAllWhoRuledInTheLargestNumberOfTerritorialUnits();
        assertThat(rulers.size()).isEqualTo(4);
        assertThat(rulers.get(0).getId()).isEqualTo(6);
        assertThat(rulers.get(1).getId()).isEqualTo(10);
        assertThat(rulers.get(2).getId()).isEqualTo(9);
        assertThat(rulers.get(3).getId()).isEqualTo(8);
    }

}
