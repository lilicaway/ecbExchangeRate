package com.liliana.sample.exchange.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.liliana.sample.exchange.model.serializer.LocalDateIso8601Deserializer;
import com.liliana.sample.exchange.model.serializer.LocalDateIso8601Serializer;

public class CurrencyConversionResult {

    @JsonSerialize(using = LocalDateIso8601Serializer.class)
    @JsonDeserialize(using = LocalDateIso8601Deserializer.class)
    private final LocalDate date;
    private final BigDecimal fromAmount;
    private final String fromCurrency;
    private final BigDecimal resultAmount;
    private final String toCurrency;

    public CurrencyConversionResult(LocalDate date, BigDecimal fromAmount, String fromCurrency,
            BigDecimal resultAmount, String toCurrency) {
        this.date = date;
        this.fromAmount = fromAmount;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.resultAmount = resultAmount;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public BigDecimal getResultAmount() {
        return resultAmount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, fromAmount, fromCurrency, resultAmount, toCurrency);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CurrencyConversionResult other = (CurrencyConversionResult) obj;
        return Objects.equals(this.date, other.date)
                && Objects.equals(this.fromAmount, other.fromAmount)
                && Objects.equals(this.fromCurrency, other.fromCurrency)
                && Objects.equals(this.resultAmount, other.resultAmount)
                && Objects.equals(this.toCurrency, other.toCurrency);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("date", this.date)
                .add("fromAmount", fromAmount).add("fromCurrency", fromCurrency)
                .add("resultAmount", resultAmount).add("toCurrency", toCurrency).toString();
    }
}
