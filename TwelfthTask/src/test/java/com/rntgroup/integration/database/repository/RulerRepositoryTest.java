package com.rntgroup.integration.database.repository;

import com.rntgroup.database.entity.Ruler;
import com.rntgroup.database.repository.RulerRepository;
import com.rntgroup.integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@IT
@Sql({
        "classpath:sql/data.sql"
})
@RequiredArgsConstructor
class RulerRepositoryTest {

    private final RulerRepository rulerRepository;

    @Test
    void findAllWhoManagedAtLeastSpecifiedNumberOfCities() {
        var rulers = rulerRepository.findAllWhoManagedAtLeast(1);
        assertEquals(5, rulers.size());

        rulers = rulerRepository.findAllWhoManagedAtLeast(2);
        assertEquals(3, rulers.size());
    }

    @Test
    void findAllWhoRuledInAllCitiesOfAnyCountry() {
        var rulers = rulerRepository.findAllWhoRuledInAllCitiesOfAnyCountry();
        assertEquals(4, rulers.size());

        var lastNames = rulers.stream().map(Ruler::getLastName).toList();

        assertThat(lastNames.contains("Кстенин"));
        assertThat(lastNames.contains("Собянин"));
        assertThat(lastNames.contains("Усов"));
        assertThat(lastNames.contains("Лужков"));
    }

    @Test
    void findPreviousRulerOfTheCity() {
        int sobyaninId = 9;
        var ruler = rulerRepository.findPreviousRulerOfTheCity(sobyaninId, "Москва");
        assertThat(ruler.isPresent());
        assertThat(ruler.get().getLastName()).isEqualTo("Лужков");

        int ksteninId = 6;
        ruler = rulerRepository.findPreviousRulerOfTheCity(ksteninId, "Воронеж");
        assertThat(ruler.isPresent());
        assertThat(ruler.get().getLastName()).isEqualTo("Апраксин");
    }

}
