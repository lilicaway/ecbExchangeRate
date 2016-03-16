package com.liliana.sample.exchange.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.convert.Jsr310Converters;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.liliana.sample.exchange.model.serializer.LocalDateIso8601Deserializer;
import com.liliana.sample.exchange.model.serializer.LocalDateIso8601Serializer;

@Entity
@Table(name = "rates", schema = "ecb_conversion")
@IdClass(value = ExchangeRate.ExchangeRateId.class)
public class ExchangeRate implements Serializable {
    @JsonSerialize(using = LocalDateIso8601Serializer.class)
    @JsonDeserialize(using = LocalDateIso8601Deserializer.class)
    // Transient because we have the getter and setter of type java.util.Date.
    @Transient
    private LocalDate date;

    private String currencyCode;

    private BigDecimal rate;

    public ExchangeRate() {
    }

    public ExchangeRate(LocalDate date, String currencyCode, BigDecimal rate) {
        this.date = date;
        this.currencyCode = currencyCode;
        this.rate = rate;
    }

    @Id
    @Column(name = "date")
    public Date getDate() {
        return Jsr310Converters.LocalDateToDateConverter.INSTANCE.convert(date);
    }

    public void setDate(Date date) {
        this.date =
                Jsr310Converters.DateToLocalDateConverter.INSTANCE.convert(date);
    }

    @Id
    @Column(name = "currency")
    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Column(name = "rate")
    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Transient
    public LocalDate getDateAsLocalDate() {
        return date;
    }

    public void setDateAsLocalDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, currencyCode, rate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
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

    public static class ExchangeRateId implements Serializable {
        private LocalDate date;
        private String currencyCode;

        public ExchangeRateId() {
        }

        public ExchangeRateId(LocalDate date, String currencyCode) {
            this.date = date;
            this.currencyCode = currencyCode;
        }

        public Date getDate() {
            return Jsr310Converters.LocalDateToDateConverter.INSTANCE.convert(date);
        }

        public void setDate(Date date) {
            this.date =
                    Jsr310Converters.DateToLocalDateConverter.INSTANCE.convert(date);
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        @Override
        public int hashCode() {
            return Objects.hash(date, currencyCode);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ExchangeRate other = (ExchangeRate) obj;
            return Objects.equals(this.date, other.date)
                    && Objects.equals(this.currencyCode, other.currencyCode);
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).add("date", date)
                    .add("currencyCode", currencyCode).toString();
        }
    }
}
