package com.liliana.sample.exchange.datasource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.xml.bind.JAXB;

import org.junit.Test;

import com.liliana.sample.exchange.datasource.ecb.generated.EnvelopeType;
import com.liliana.sample.exchange.model.ExchangeRate;

public class EnvelopeTypeToExchangeRatesTranformerTest {

    @Test
    public void testTransforms() {
        InputStream example_eurofx_file = EnvelopeTypeToExchangeRatesTranformerTest.class
                .getResourceAsStream("/example_eurofx.xml");

        EnvelopeType object = JAXB.unmarshal(example_eurofx_file, EnvelopeType.class);

        EnvelopeTypeToExchangeRatesTranformer tranformer = new EnvelopeTypeToExchangeRatesTranformer();

        List<ExchangeRate> rates = tranformer.apply(object);

        LocalDate day18 = LocalDate.of(2016, 2, 18);
        LocalDate day17 = LocalDate.of(2016, 2, 17);
        assertThat(rates, contains(
                new ExchangeRate(day18, "USD", BigDecimal.valueOf(1.1084)),
                new ExchangeRate(day18, "JPY", BigDecimal.valueOf(126.17)),
                new ExchangeRate(day17, "USD", BigDecimal.valueOf(1.1136)),
                new ExchangeRate(day17, "JPY", BigDecimal.valueOf(127.1))));
    }

}
