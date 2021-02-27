package com.makotora.cardcostapi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.makotora.cardcostapi.dto.CountryClearingCostDTO;

@Repository
public interface CountryClearingCostDAO extends JpaRepository<CountryClearingCostDTO, String>
{}
