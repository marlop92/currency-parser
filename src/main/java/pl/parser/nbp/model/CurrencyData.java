package pl.parser.nbp.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CurrencyData {

    private String currency;
    private LocalDate publicationDate;
    private BigDecimal purchasePrice;
    private BigDecimal salesPrice;

    public CurrencyData(String currency, LocalDate publicationDate, BigDecimal purchasePrice, BigDecimal salesPrice) {
        this.currency = currency;
        this.publicationDate = publicationDate;
        this.purchasePrice = purchasePrice;
        this.salesPrice = salesPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }
}
