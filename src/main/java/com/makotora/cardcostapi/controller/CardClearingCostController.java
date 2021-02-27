package com.makotora.cardcostapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.makotora.cardcostapi.constants.APIPaths;
import com.makotora.cardcostapi.controller.request.CardClearingCostRequest;
import com.makotora.cardcostapi.controller.response.CardClearingCostResponse;
import com.makotora.cardcostapi.exception.APIException;
import com.makotora.cardcostapi.service.CardClearingCostService;

@RestController
public class CardClearingCostController
{
    private final Logger log;

    private final CardClearingCostService cardClearingCostService;

    @Autowired
    public CardClearingCostController(CardClearingCostService cardClearingCostService)
    {
        log = LoggerFactory.getLogger(CardClearingCostController.class);
        this.cardClearingCostService = cardClearingCostService;
    }

    @RequestMapping(path = APIPaths.CARD_CLEARING_COST_PATH, method = RequestMethod.POST)
    public @ResponseBody CardClearingCostResponse determineClearingCost(
        @RequestBody CardClearingCostRequest cardClearingCostRequest)
        throws APIException
    {
        log.debug("determineClearingCost [cardClearingCostRequest={}]", cardClearingCostRequest);

        return cardClearingCostService.determineClearingCost(cardClearingCostRequest.getCardNumber());
    }
}
