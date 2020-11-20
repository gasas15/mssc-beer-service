package com.saimirgasa.msscbeerservice.web.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saimirgasa.msscbeerservice.web.model.BeerDto;
import com.saimirgasa.msscbeerservice.web.model.BeerStyleEnum;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "dev.com.saimirgasa", uriPort = 80)
@WebMvcTest
@ComponentScan(basePackages = "com.saimirgasa.msscbeerservice.web.mappers")
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getBeerById() throws Exception {

        mockMvc.perform(get("/api/v1/beer/{beerId}", UUID.randomUUID().toString())
            .param("isCold", "yes")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("v1/beer-get",
                pathParameters(
                    parameterWithName("beerId")
                    .description("UUID of desired beer to get.")
                ),
                requestParameters(
                    parameterWithName("isCold").description("Is Beer Cold Query param")
                ),
                responseFields(
                    fieldWithPath("id").description("Id of Beer."),
                    fieldWithPath("version").description("Version number."),
                    fieldWithPath("createdDate").description("Date Created."),
                    fieldWithPath("lastModifiedDate").description("Date Updated."),
                    fieldWithPath("beerName").description("Name of the Beer."),
                    fieldWithPath("beerStyle").description("Style of the Beer."),
                    fieldWithPath("upc").description("UPC of the Beer."),
                    fieldWithPath("price").description("Price of the Beer."),
                    fieldWithPath("quantityOnHand").description("Quantity On Hand.")
                )));
    }

    @Test
    void saveNewBeer() throws Exception {
        BeerDto beerDto = getValidBeerDto();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        ConstrainedFields fields = new ConstrainedFields(BeerDto.class);

        mockMvc.perform(post("/api/v1/beer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
            .andExpect(status().isCreated())
            .andDo(document("v1/beer-new",
                requestFields(
                    fields.withPath("id").ignored(),
                    fields.withPath("version").ignored(),
                    fields.withPath("createdDate").ignored(),
                    fields.withPath("lastModifiedDate").ignored(),
                    fields.withPath("beerName").description("Name of the Beer"),
                    fields.withPath("beerStyle").description("Style of the Beer"),
                    fields.withPath("upc").description("UPC of the Beer"),
                    fields.withPath("price").description("Price of the Beer"),
                    fields.withPath("quantityOnHand").ignored()
                )));
    }

    @Test
    void updateBeerById() throws Exception {
        BeerDto beerDto = getValidBeerDto();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        mockMvc.perform(put("/api/v1/beer/" + UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
            .andExpect(status().isNoContent());
    }

    private BeerDto getValidBeerDto() {

        return BeerDto.builder()
                .beerName("My Beer")
                .beerStyle(BeerStyleEnum.ALE)
                .price(new BigDecimal("2.99"))
                .upc(1231231232132L)
            .build();
    }

    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                .collectionToDelimitedString(this.constraintDescriptions
                    .descriptionsForProperty(path), ". ")));
        }
    }
}
