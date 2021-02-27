package com.makotora.cardcostapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import com.makotora.cardcostapi.controller.request.CountryClearingCostCreateRequest;
import com.makotora.cardcostapi.controller.request.CountryClearingCostUpdateRequest;
import com.makotora.cardcostapi.dao.CountryClearingCostDAO;
import com.makotora.cardcostapi.dto.CountryClearingCostDTO;
import com.makotora.cardcostapi.service.CountryClearingCostService;

@SpringBootTest
@WebAppConfiguration
public class CountryClearingCostControllerTest
{
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private CountryClearingCostDAO countryClearingCostDAO;

    @Autowired
    private CountryClearingCostService countryClearingCostService;

    private MockMvc mockMvc;

    private String testCountryCode;

    private BigDecimal testClearingCost;

    private String notExistsCountryCode;

    @BeforeEach
    @Transactional
    public void setup()
        throws Exception
    {
        testCountryCode = "TT";
        testClearingCost = BigDecimal.valueOf(12.34);

        {
            Optional<CountryClearingCostDTO> testCountryClearingCost = countryClearingCostDAO.findById(testCountryCode);
            if (testCountryClearingCost.isPresent()) {
                countryClearingCostDAO.delete(testCountryClearingCost.get());
            }
        }

        {
            notExistsCountryCode = "XX";
            Optional<CountryClearingCostDTO> notExistsCountryClearingCost = countryClearingCostDAO.findById(notExistsCountryCode);
            if (notExistsCountryClearingCost.isPresent()) {
                countryClearingCostDAO.delete(notExistsCountryClearingCost.get());
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
    public void create_success()
        throws Exception
    {
        CountryClearingCostCreateRequest createRequest = new CountryClearingCostCreateRequest();
        createRequest.setCountryCode(testCountryCode);
        createRequest.setCost(testClearingCost);

        String requestJson = getRequestJson(createRequest);

        this.mockMvc.perform(post(APIPaths.COUNTRY_CLEARING_COST_PATH).contentType(MediaType.APPLICATION_JSON)
                                                                      .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.countryCode").value(testCountryCode))
                    .andExpect(jsonPath("$.cost").value(testClearingCost))
                    .andReturn();
    }

    @Test
    public void create_already_exists()
        throws Exception
    {
        countryClearingCostService.create(testCountryCode, testClearingCost);

        CountryClearingCostCreateRequest createRequest = new CountryClearingCostCreateRequest();
        createRequest.setCountryCode(testCountryCode);
        createRequest.setCost(testClearingCost);

        String requestJson = getRequestJson(createRequest);

        this.mockMvc.perform(post(APIPaths.COUNTRY_CLEARING_COST_PATH).contentType(MediaType.APPLICATION_JSON)
                                                                      .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andReturn();
    }

    @Test
    public void update_success()
        throws Exception
    {
        {
            BigDecimal oldCost = BigDecimal.valueOf(12.34);
            countryClearingCostService.create(testCountryCode, oldCost);
        }

        BigDecimal newCost = BigDecimal.valueOf(55.66);

        CountryClearingCostUpdateRequest updateRequest = new CountryClearingCostUpdateRequest();
        updateRequest.setCost(newCost);

        String requestJson = getRequestJson(updateRequest);

        String updatePath = APIPaths.COUNTRY_CLEARING_COST_PATH + "/" + testCountryCode;

        this.mockMvc.perform(put(updatePath).contentType(MediaType.APPLICATION_JSON).content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.countryCode").value(testCountryCode))
                    .andExpect(jsonPath("$.cost").value(newCost))
                    .andReturn();
    }

    @Test
    public void update_not_found()
        throws Exception
    {
        BigDecimal newCost = BigDecimal.valueOf(55.66);

        CountryClearingCostUpdateRequest updateRequest = new CountryClearingCostUpdateRequest();
        updateRequest.setCost(newCost);

        String requestJson = getRequestJson(updateRequest);

        String updatePath = APIPaths.COUNTRY_CLEARING_COST_PATH + "/" + notExistsCountryCode;

        this.mockMvc.perform(put(updatePath).contentType(MediaType.APPLICATION_JSON).content(requestJson))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andReturn();
    }

    @Test
    public void delete_success()
        throws Exception
    {
        countryClearingCostService.create(testCountryCode, testClearingCost);

        String deletePath = APIPaths.COUNTRY_CLEARING_COST_PATH + "/" + testCountryCode;

        this.mockMvc.perform(delete(deletePath))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();
    }

    @Test
    public void delete_not_found()
        throws Exception
    {
        String deletePath = APIPaths.COUNTRY_CLEARING_COST_PATH + "/" + notExistsCountryCode;

        this.mockMvc.perform(delete(deletePath))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andReturn();
    }

    @Test
    public void view_success()
        throws Exception
    {
        countryClearingCostService.create(testCountryCode, testClearingCost);

        String viewPath = APIPaths.COUNTRY_CLEARING_COST_PATH + "/" + testCountryCode;

        this.mockMvc.perform(get(viewPath)).andDo(print()).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void view_not_found()
        throws Exception
    {
        String deletePath = APIPaths.COUNTRY_CLEARING_COST_PATH + "/" + notExistsCountryCode;

        this.mockMvc.perform(get(deletePath)).andDo(print()).andExpect(status().isNotFound()).andReturn();
    }

}
