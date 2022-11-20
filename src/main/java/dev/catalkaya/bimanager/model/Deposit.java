package dev.catalkaya.bimanager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.LocalDateTime;

@JsonDeserialize(builder = Deposit.Builder.class)
public class Deposit {
    @JsonProperty("deposit_id")
    private String depositId;
    @JsonProperty("person_id")
    private String personId;
    @JsonProperty("deposit_datetime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", shape = JsonFormat.Shape.STRING)
    private LocalDateTime depositDatetime;
    @JsonProperty("deposit_amount")
    private Double depositAmount;
    @JsonProperty("deposit_method")
    private String depositMethod;
    @JsonProperty("deposit_after")
    @JsonIgnore
    private LocalDateTime depositAfter;

    public Deposit(String depositId, String personId, LocalDateTime depositDatetime, Double depositAmount, String depositMethod, LocalDateTime depositAfter) {
        this.depositId = depositId;
        this.personId = personId;
        this.depositDatetime = depositDatetime;
        this.depositAmount = depositAmount;
        this.depositMethod = depositMethod;
        this.depositAfter = depositAfter;
    }

    private Deposit(Builder builder) {
        depositId = builder.depositId;
        personId = builder.personId;
        depositDatetime = builder.depositDatetime;
        depositAmount = builder.depositAmount;
        depositMethod = builder.depositMethod;
        depositAfter = builder.depositAfter;
    }

    @JsonPOJOBuilder
    public static final class Builder {
        @JsonProperty("deposit_id")
        private String depositId;
        @JsonProperty("person_id")
        private String personId;
        @JsonProperty("deposit_datetime")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", shape = JsonFormat.Shape.STRING)
        private LocalDateTime depositDatetime;
        @JsonProperty("deposit_amount")
        private Double depositAmount;
        @JsonProperty("deposit_method")
        private String depositMethod;
        @JsonProperty("deposit_after")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", shape = JsonFormat.Shape.STRING)
        private LocalDateTime depositAfter;

        public Builder() {
        }

        public Builder withDepositId(String val) {
            depositId = val;
            return this;
        }

        public Builder withPersonId(String val) {
            personId = val;
            return this;
        }

        public Builder withDepositDatetime(LocalDateTime val) {
            depositDatetime = val;
            return this;
        }

        public Builder withDepositAmount(Double val) {
            depositAmount = val;
            return this;
        }

        public Builder withDepositMethod(String val) {
            depositMethod = val;
            return this;
        }

        public Builder withDepositAfter(LocalDateTime val) {
            depositAfter = val;
            return this;
        }

        public Deposit build() {
            return new Deposit(this);
        }
    }

    public String getDepositId() {
        return depositId;
    }

    public void setDepositId(String depositId) {
        this.depositId = depositId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public LocalDateTime getDepositDatetime() {
        return depositDatetime;
    }

    public void setDepositDatetime(LocalDateTime depositDatetime) {
        this.depositDatetime = depositDatetime;
    }

    public Double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(Double depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getDepositMethod() {
        return depositMethod;
    }

    public void setDepositMethod(String depositMethod) {
        this.depositMethod = depositMethod;
    }

    public LocalDateTime getDepositAfter() {
        return depositAfter;
    }

    public void setDepositAfter(LocalDateTime depositAfter) {
        this.depositAfter = depositAfter;
    }
}
