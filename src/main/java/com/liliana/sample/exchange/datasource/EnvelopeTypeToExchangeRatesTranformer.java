package com.liliana.sample.exchange.datasource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.liliana.sample.exchange.datasource.ecb.generated.CurrencyCube;
import com.liliana.sample.exchange.datasource.ecb.generated.EnvelopeType;
import com.liliana.sample.exchange.datasource.ecb.generated.TimeCube;
import com.liliana.sample.exchange.model.ExchangeRate;

@Component
public class EnvelopeTypeToExchangeRatesTranformer implements
        Function<EnvelopeType, List<ExchangeRate>> {

    @Override
    public List<ExchangeRate> apply(EnvelopeType t) {
        List<ExchangeRate> rates = new ArrayList<>();
        for (TimeCube timeCube : t.getCube().getCube()) {
            LocalDate date = LocalDate.of(
                    timeCube.getTime().getYear(),
                    timeCube.getTime().getMonth(),
                    timeCube.getTime().getDay());
            for (CurrencyCube currencyCube : timeCube.getCube()) {
                // We do it like this, because getRate returns a Float and when
                // using BigDecimal.valueOf method, it converts that into a
                // double, which can mess up with the value. For example, the
                // float 1.1084 gets converted to 1.1083999872207642 when
                // converted to double. So it is safer to do a toString of the
                // Float and use that.
                BigDecimal rate = new BigDecimal(currencyCube.getRate().toString());
                ExchangeRate exchangeRate = new ExchangeRate(date, currencyCube.getCurrency(), rate);
                rates.add(exchangeRate);
            }
        }
        return rates;
    }

}
