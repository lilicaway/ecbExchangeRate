package com.liliana.sample.exchange.service.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.google.common.base.MoreObjects;
import com.liliana.sample.exchange.service.repository.ExchangeRateEntity.ExchangeRateId;

@Entity
@Table(name = "rates", schema = "ecb_conversion")
@IdClass(value = ExchangeRateId.class)
public class ExchangeRateEntity implements Serializable {
    private Date date;

    private String currencyCode;

    private BigDecimal rate;

    @Id
    @Column(name = "date")
    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
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
        ExchangeRateEntity other = (ExchangeRateEntity) obj;
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
        private Date date;
        private String currencyCode;

        public ExchangeRateId() {
        }

        public ExchangeRateId(Date date, String currencyCode) {
            this.date = date;
            this.currencyCode = currencyCode;
        }

        public Date getDate() {
            return this.date;
        }

        public void setDate(Date date) {
            this.date = date;
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
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ExchangeRateEntity other = (ExchangeRateEntity) obj;
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
