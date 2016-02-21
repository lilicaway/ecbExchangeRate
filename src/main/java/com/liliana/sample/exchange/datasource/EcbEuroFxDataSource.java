package com.liliana.sample.exchange.datasource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.liliana.sample.exchange.datasource.ecb.generated.EnvelopeType;
import com.liliana.sample.exchange.model.ExchangeRate;

@Service
public class EcbEuroFxDataSource {

    @Autowired
    EnvelopeTypeToExchangeRatesTranformer dataTranformer;

    @Autowired
    RestTemplate restTemplate;

    private List<ExchangeRate> readFromDataSource(String fileSuffix) {
        EnvelopeType envelopeType = restTemplate.getForObject(
                "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-{fileSuffix}.xml",
                EnvelopeType.class, fileSuffix);
        return dataTranformer.apply(envelopeType);
    }

    public List<ExchangeRate> readDailyRates() {
        return readFromDataSource("daily");
    }

    public List<ExchangeRate> readLast90DaysRates() {
        return readFromDataSource("hist-90d");
    }
}
