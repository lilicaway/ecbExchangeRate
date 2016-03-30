package com.liliana.sample.exchange.service.repository;

import org.springframework.data.repository.CrudRepository;

import com.liliana.sample.exchange.service.repository.ExchangeRateEntity.ExchangeRateId;

public interface ExchangeRateRepository extends
        CrudRepository<ExchangeRateEntity, ExchangeRateId> {

    // We don't really need to declare these methods here, since they are
    // inherited anyway. We only add them here as documentation because they are
    // the methods we care about.
    @Override
    public <S extends ExchangeRateEntity> S save(S entity);

    @Override
    public ExchangeRateEntity findOne(ExchangeRateId id);

    @Override
    public <S extends ExchangeRateEntity> Iterable<S> save(Iterable<S> entities);
}
