package com.makotora.cardcostapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.makotora.cardcostapi.constants.APIPaths;
import com.makotora.cardcostapi.controller.request.CardClearingCostRequest;
import com.makotora.cardcostapi.dao.CountryClearingCostDAO;
import com.makotora.cardcostapi.dto.CountryClearingCostDTO;
import com.makotora.cardcostapi.service.CountryClearingCostService;

@SpringBootTest
@WebAppConfiguration
public class CardClearingCostControllerTest
{
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private CountryClearingCostDAO countryClearingCostDAO;

    @Autowired
    private CountryClearingCostService countryClearingCostService;

    private MockMvc mockMvc;

    private String testCountryCode;

    private String testCardNumber;

    private BigDecimal testClearingCost;

    @BeforeEach
    @Transactional
    public void setup()
        throws Exception
    {
        testCountryCode = "DK";
        testClearingCost = BigDecimal.valueOf(12.34);
        testCardNumber = "4571 7360 1234 1234"; // Binlist API should respond DK to this

        {
            Optional<CountryClearingCostDTO> testCountryClearingCost = countryClearingCostDAO.findById(testCountryCode);
            if (testCountryClearingCost.isPresent()) {
                countryClearingCostDAO.delete(testCountryClearingCost.get());
            }
        }

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    private String getRequestJson(Object obj)
        throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(obj);

        return requestJson;
    }

    @Test
    public void determineClearingCost_success()
        throws Exception
    {
        countryClearingCostService.create(testCountryCode, testClearingCost);

        CardClearingCostRequest cardClearingCostRequest = new CardClearingCostRequest();
        cardClearingCostRequest.setCardNumber(testCardNumber);

        String requestJson = getRequestJson(cardClearingCostRequest);

        this.mockMvc.perform(post(APIPaths.CARD_CLEARING_COST_PATH).contentType(MediaType.APPLICATION_JSON)
                                                                      .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.country").value(testCountryCode))
                    .andExpect(jsonPath("$.cost").value(testClearingCost))
                    .andReturn();
    }

    @Test
    public void determineClearingCost_country_code_cost_not_found()
        throws Exception
    {
        CardClearingCostRequest cardClearingCostRequest = new CardClearingCostRequest();
        cardClearingCostRequest.setCardNumber(testCardNumber);

        String requestJson = getRequestJson(cardClearingCostRequest);

        this.mockMvc.perform(post(APIPaths.CARD_CLEARING_COST_PATH).contentType(MediaType.APPLICATION_JSON)
                                                                   .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andReturn();
    }

}
