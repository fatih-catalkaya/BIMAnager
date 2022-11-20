package dev.catalkaya.bimanager.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Optional;

@JsonDeserialize(builder = Product.Builder.class)
@JsonPropertyOrder({"product_id", "product_name", "product_price"})
public class Product {
    @JsonProperty("product_id")
    private String productId;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("product_price")
    private Double productPrice;

    public Product(String productId, String productName, Double productPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    private Product(Builder builder) {
        productId = builder.productId;
        productName = builder.productName;
        productPrice = builder.productPrice;
    }

    @JsonPOJOBuilder
    public static final class Builder {
        @JsonProperty("product_id")
        private String productId;
        @JsonProperty("product_name")
        private String productName;
        @JsonProperty("product_price")
        private Double productPrice = Double.NaN;

        public Builder() {
        }

        public Builder withProductId(String val) {
            productId = val;
            return this;
        }

        public Builder withProductName(String val) {
            productName = val;
            return this;
        }

        public Builder withProductPrice(Double val) {
            productPrice = val;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }
}