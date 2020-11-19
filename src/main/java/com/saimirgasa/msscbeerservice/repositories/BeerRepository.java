package com.saimirgasa.msscbeerservice.repositories;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.saimirgasa.msscbeerservice.domain.Beer;

public interface BeerRepository extends PagingAndSortingRepository<Beer, UUID> {

}
