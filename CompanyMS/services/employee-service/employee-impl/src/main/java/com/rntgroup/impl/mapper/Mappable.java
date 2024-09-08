package com.rntgroup.impl.mapper;

public interface Mappable<E, D> {
    D toDto(E entity);

    E toEntity(D dto);
}
