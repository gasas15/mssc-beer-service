package com.saimirgasa.msscbeerservice.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.saimirgasa.msscbeerservice.domain.Beer;
import com.saimirgasa.msscbeerservice.repositories.BeerRepository;
import com.saimirgasa.msscbeerservice.web.controller.NotFoundException;
import com.saimirgasa.msscbeerservice.web.mappers.BeerMapper;
import com.saimirgasa.msscbeerservice.web.model.BeerDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public BeerDto getById(UUID beerId) {
        return beerMapper.beerToBeerDto(
            beerRepository.findById(beerId).orElseThrow(NotFoundException::new)
        );
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {
        return beerMapper.beerToBeerDto(
            beerRepository.save(
                beerMapper.beerDtoToBeer(beerDto)
            )
        );
    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {

        Beer beer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);

        beer.setBeerName(beerDto.getBeerName());
        beer.setBeerStyle(beerDto.getBeerStyle().name());
        beer.setPrice(beerDto.getPrice());
        beer.setUpc(beerDto.getUpc());

        return beerMapper.beerToBeerDto(beerRepository.save(beer));
    }
}
