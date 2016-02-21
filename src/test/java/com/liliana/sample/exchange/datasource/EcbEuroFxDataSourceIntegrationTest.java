package com.liliana.sample.exchange.datasource;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.liliana.sample.exchange.model.ExchangeRate;

/**
 * Note that this Integration test accesses network which is in general a bad
 * idea. But here we want a way of independently making a call to the real ECB
 * services and making sure we can parse their results.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DatasourceTestConfiguration.class)
public class EcbEuroFxDataSourceIntegrationTest {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    EcbEuroFxDataSource dataSource;

    @Test
    public void testReadDailyRates() {
        List<ExchangeRate> dailyRates = dataSource.readDailyRates();
        log.info("dailyRates: " + dailyRates);
        assertNotNull(dailyRates);
        assertThat(dailyRates.size(), is(greaterThan(1)));
    }

    @Test
    public void testReadLast90DaysRates() {
        List<ExchangeRate> last90days = dataSource.readLast90DaysRates();
        log.info("last90days: " + last90days);
        assertNotNull(last90days);
        assertThat(last90days.size(), is(greaterThan(1)));
    }

}
