package pl.parser.nbp.model;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class CurrencyData {

    private String currency;
    private LocalDate publicationDate;
    private BigDecimal purchasePrice;
    private BigDecimal salesPrice;

    public CurrencyData() {

    }

    public CurrencyData(String currency, LocalDate publicationDate, BigDecimal purchasePrice, BigDecimal salesPrice) {
        this.currency = currency;
        this.publicationDate = publicationDate;
        this.purchasePrice = purchasePrice;
        this.salesPrice = salesPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(int divisor, BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice.divide(new BigDecimal(divisor));
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(int divisor, BigDecimal salesPrice) {
        this.salesPrice = salesPrice.divide(new BigDecimal(divisor));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyData data = (CurrencyData) o;
        return Objects.equals(currency, data.currency) &&
                Objects.equals(publicationDate, data.publicationDate) &&
                Objects.equals(purchasePrice, data.purchasePrice) &&
                Objects.equals(salesPrice, data.salesPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, publicationDate, purchasePrice, salesPrice);
    }

    @Override
    public String toString() {
        return "CurrencyData{" +
                "currency='" + currency + '\'' +
                ", publicationDate=" + publicationDate +
                ", purchasePrice=" + purchasePrice +
                ", salesPrice=" + salesPrice +
                '}';
    }
}
