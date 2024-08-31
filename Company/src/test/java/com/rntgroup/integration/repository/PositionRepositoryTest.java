package com.rntgroup.integration.repository;

import com.rntgroup.database.repository.PositionRepository;
import com.rntgroup.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RequiredArgsConstructor
public class PositionRepositoryTest extends IntegrationTestBase {

    private final PositionRepository positionRepository;

    @Test
    void findAllByPageable() {
        var pageable = PageRequest.of(0, 5,
                Sort.by(Sort.Direction.ASC, "name"));
        var positions = positionRepository.findAllBy(pageable).getContent();

        assertThat(positions.size()).isEqualTo(5);
        assertThat(positions.get(0).getId()).isEqualTo(4);
        assertThat(positions.get(1).getId()).isEqualTo(2);
        assertThat(positions.get(2).getId()).isEqualTo(5);
        assertThat(positions.get(3).getId()).isEqualTo(9);
        assertThat(positions.get(4).getId()).isEqualTo(8);
    }
}
