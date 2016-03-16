package com.liliana.sample.exchange.service.repository;

import org.springframework.data.repository.CrudRepository;

import com.liliana.sample.exchange.model.ExchangeRate;
import com.liliana.sample.exchange.model.ExchangeRate.ExchangeRateId;

public interface ExchangeRateRepository extends
        CrudRepository<ExchangeRate, ExchangeRate.ExchangeRateId> {

    @Override
    public <S extends ExchangeRate> S save(S entity);

    @Override
    public ExchangeRate findOne(ExchangeRateId id);

    @Override
    public <S extends ExchangeRate> Iterable<S> save(Iterable<S> entities);
}
