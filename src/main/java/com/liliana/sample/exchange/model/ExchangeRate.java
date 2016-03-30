package com.liliana.sample.exchange.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.liliana.sample.exchange.model.serializer.LocalDateIso8601Deserializer;
import com.liliana.sample.exchange.model.serializer.LocalDateIso8601Serializer;

public class ExchangeRate implements Serializable {
    @JsonSerialize(using = LocalDateIso8601Serializer.class)
    @JsonDeserialize(using = LocalDateIso8601Deserializer.class)
    private LocalDate date;

    private String currencyCode;

    private BigDecimal rate;

    public ExchangeRate(LocalDate date, String currencyCode, BigDecimal rate) {
        this.date = date;
        this.currencyCode = currencyCode;
        this.rate = rate;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, currencyCode, rate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ExchangeRate other = (ExchangeRate) obj;
        return Objects.equals(this.date, other.date)
                && Objects.equals(this.currencyCode, other.currencyCode)
                && Objects.equals(this.rate, other.rate);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("date", date).add("currencyCode", currencyCode)
                .add("rate", rate).toString();
    }

}
