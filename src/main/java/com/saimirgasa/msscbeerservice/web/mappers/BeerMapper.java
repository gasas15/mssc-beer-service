package com.saimirgasa.msscbeerservice.web.mappers;

import org.mapstruct.Mapper;

import com.saimirgasa.msscbeerservice.domain.Beer;
import com.saimirgasa.msscbeerservice.web.model.BeerDto;

@Mapper(uses = {DateMapper.class})
public interface BeerMapper {

    BeerDto beerToBeerDto(Beer beer);

    Beer beerDtoToBeer(BeerDto beerDto);
}
