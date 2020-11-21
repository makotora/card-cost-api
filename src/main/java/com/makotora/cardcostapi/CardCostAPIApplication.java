package com.makotora.cardcostapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.makotora.cardcostapi.dto")
@EnableJpaRepositories("com.makotora.cardcostapi.dao")
@ComponentScan("com.makotora.cardcostapi")
public class CardCostAPIApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(CardCostAPIApplication.class, args);
    }
}
