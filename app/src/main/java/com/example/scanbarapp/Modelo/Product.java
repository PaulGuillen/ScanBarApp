package com.example.scanbarapp.Modelo;

import java.util.ArrayList;

public class Product {

    String ProductScanNumber;
    String productDescription;
    String productName;
    String productPurchase;
    String productQuantity;
    String productSell;
    String productType;
    String currentDate;
    String currentTime;
    float productPackaging;
    public Product() {
    }

    public Product(String productScanNumber, String productDescription, String productName,
                   String productPurchase, String productQuantity, String productSell, String productType
            , String currentDate, String currentTime, float productPackaging) {
        ProductScanNumber = productScanNumber;
        this.productDescription = productDescription;
        this.productName = productName;
        this.productPurchase = productPurchase;
        this.productQuantity = productQuantity;
        this.productSell = productSell;
        this.productType = productType;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.productPackaging = productPackaging;
    }


    public String getProductSell() {
        return productSell;
    }

    public void setProductSell(String productSell) {
        this.productSell = productSell;
    }

    public String getProductScanNumber() {
        return ProductScanNumber;
    }

    public void setProductScanNumber(String productScanNumber) {
        ProductScanNumber = productScanNumber;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPurchase() {
        return productPurchase;
    }

    public void setProductPurchase(String productPurchase) {
        this.productPurchase = productPurchase;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }


    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public float getProductPackaging() {
        return productPackaging;
    }

    public void setProductPackaging(float productPackaging) {
        this.productPackaging = productPackaging;
    }


}
