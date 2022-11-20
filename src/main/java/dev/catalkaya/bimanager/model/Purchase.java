package dev.catalkaya.bimanager.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.LocalDateTime;

@JsonDeserialize(builder = Purchase.Builder.class)
@JsonPropertyOrder({"purchase_id", "person_id", "purchase_datetime", "purchase_product_name", "purchase_product_price", "purchase_amount"})
public class Purchase {
    @JsonProperty("purchase_id")
    private String purchaseId;
    @JsonProperty("person_id")
    private String personId;
    @JsonProperty("purchase_product_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String purchaseProductId;
    @JsonProperty("purchase_datetime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", shape = JsonFormat.Shape.STRING)
    private LocalDateTime purchaseDatetime;
    @JsonProperty("purchase_product_name")
    private String purchaseProductName;
    @JsonProperty("purchase_product_price")
    private Double purchaseProductPrice;
    @JsonProperty("purchase_amount")
    private int purchaseAmount;
    @JsonProperty("purchase_after")
    @JsonIgnore
    private LocalDateTime purchaseAfter;


    public Purchase(String purchaseId, String personId, String purchaseProductId, LocalDateTime purchaseDatetime, String purchaseProductName, Double purchaseProductPrice, int purchaseAmount) {
        this.purchaseId = purchaseId;
        this.personId = personId;
        this.purchaseProductId = purchaseProductId;
        this.purchaseDatetime = purchaseDatetime;
        this.purchaseProductName = purchaseProductName;
        this.purchaseProductPrice = purchaseProductPrice;
        this.purchaseAmount = purchaseAmount;
    }

    private Purchase(Builder builder) {
        purchaseId = builder.purchaseId;
        personId = builder.personId;
        purchaseProductId = builder.purchaseProductId;
        purchaseDatetime = builder.purchaseDatetime;
        purchaseProductName = builder.purchaseProductName;
        purchaseProductPrice = builder.purchaseProductPrice;
        purchaseAmount = builder.purchaseAmount;
        purchaseAfter = builder.purchaseAfter;
    }

    @JsonPOJOBuilder
    public static final class Builder {
        @JsonProperty("purchase_id")
        private String purchaseId;
        @JsonProperty("person_id")
        private String personId;
        @JsonProperty("purchase_product_id")
        private String purchaseProductId;
        @JsonProperty("purchase_datetime")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", shape = JsonFormat.Shape.STRING)
        private LocalDateTime purchaseDatetime;
        @JsonProperty("purchase_product_name")
        private String purchaseProductName;
        @JsonProperty("purchase_product_price")
        private Double purchaseProductPrice;
        @JsonProperty("purchase_amount")
        private int purchaseAmount;
        @JsonProperty("purchase_after")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", shape = JsonFormat.Shape.STRING)
        private LocalDateTime purchaseAfter;

        public Builder() {
        }

        public Builder withPurchaseId(String val) {
            purchaseId = val;
            return this;
        }

        public Builder withPersonId(String val) {
            personId = val;
            return this;
        }

        public Builder withPurchaseProductId(String val) {
            purchaseProductId = val;
            return this;
        }

        public Builder withPurchaseDatetime(LocalDateTime val) {
            purchaseDatetime = val;
            return this;
        }

        public Builder withPurchaseProductName(String val) {
            purchaseProductName = val;
            return this;
        }

        public Builder withPurchaseProductPrice(Double val) {
            purchaseProductPrice = val;
            return this;
        }

        public Builder withPurchaseAmount(int val) {
            purchaseAmount = val;
            return this;
        }

        public Builder withPurchaseAfter(LocalDateTime val){
            purchaseAfter = val;
            return this;
        }

        public Purchase build() {
            return new Purchase(this);
        }
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPurchaseProductId() {
        return purchaseProductId;
    }

    public void setPurchaseProductId(String purchaseProductId) {
        this.purchaseProductId = purchaseProductId;
    }

    public LocalDateTime getPurchaseDatetime() {
        return purchaseDatetime;
    }

    public void setPurchaseDatetime(LocalDateTime purchaseDatetime) {
        this.purchaseDatetime = purchaseDatetime;
    }

    public String getPurchaseProductName() {
        return purchaseProductName;
    }

    public void setPurchaseProductName(String purchaseProductName) {
        this.purchaseProductName = purchaseProductName;
    }

    public Double getPurchaseProductPrice() {
        return purchaseProductPrice;
    }

    public void setPurchaseProductPrice(Double purchaseProductPrice) {
        this.purchaseProductPrice = purchaseProductPrice;
    }

    public int getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(int purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public LocalDateTime getPurchaseAfter() {
        return purchaseAfter;
    }

    public void setPurchaseAfter(LocalDateTime purchaseAfter) {
        this.purchaseAfter = purchaseAfter;
    }
}
