package com.liliana.sample.exchange.scheduler;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.liliana.sample.exchange.datasource.EcbEuroFxDataSource;
import com.liliana.sample.exchange.model.ExchangeRate;
import com.liliana.sample.exchange.service.ExchangeRateDao;

@Component
public class UpdaterScheduler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ExchangeRateDao exchangeRateDao;

    @Autowired
    @Qualifier("ecbRetryTemplate")
    RetryTemplate retryTemplate;

    @Autowired
    EcbEuroFxDataSource ecbEuroFxds;

    /**
     * We update the last 90 days information only once, when the application
     * starts. This includes the latest day, so we only need to update the daily
     * rates once a day after this.
     */
    @PostConstruct
    public void updateLast90Days() {
        List<ExchangeRate> readLast90DaysRates = retryTemplate
                .execute(new RetryCallback<List<ExchangeRate>, RuntimeException>() {
                    @Override
                    public List<ExchangeRate> doWithRetry(RetryContext context)
                            throws RuntimeException {
                        log.info("Attempting to read last 90 days rates...");
                        return ecbEuroFxds.readLast90DaysRates();
                    }
                });
        log.info("Updating last 90 days rates: " + readLast90DaysRates);
        exchangeRateDao.saveAllExchangeRate(readLast90DaysRates);
    }

    /**
     * Updates the daily rates which according to
     * http://www.ecb.europa.eu/stats/exchange/eurofxref/html/index.en.html are
     * updated at around 16:00 CET.
     * <p>
     * This could be weak and fail to update if the ECB don't update the rates
     * on time. We might have to tweak the cron expression or add some extra
     * logic to detect if the info is outdated and in this case retry.
     */
    @Scheduled(cron = "10 16 * * * MON-FRI", zone = "CET")
    public void updateDaily() {
        List<ExchangeRate> readDailyRates = retryTemplate
                .execute(new RetryCallback<List<ExchangeRate>, RuntimeException>() {
                    @Override
                    public List<ExchangeRate> doWithRetry(RetryContext context)
                            throws RuntimeException {
                        log.info("Attempting to read daily rates...");
                        return ecbEuroFxds.readDailyRates();
                    }
                });
        log.info("Updating daily rates: " + readDailyRates);
        exchangeRateDao.saveAllExchangeRate(readDailyRates);
    }
}
