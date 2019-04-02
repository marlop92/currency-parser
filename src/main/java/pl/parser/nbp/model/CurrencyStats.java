package pl.parser.nbp.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class CurrencyStats {

    private String currency;
    private BigDecimal avgPurchasePrice;
    private BigDecimal salesStandardDeviation;
    private LocalDate statsBeginDate;
    private LocalDate statsEndDate;

    public CurrencyStats(String currency, BigDecimal avgPurchasePrice, BigDecimal salesStandardDeviation,
                         LocalDate statsBeginDate, LocalDate statsEndDate) {
        this.currency = currency;
        this.avgPurchasePrice = avgPurchasePrice;
        this.salesStandardDeviation = salesStandardDeviation;
        this.statsBeginDate = statsBeginDate;
        this.statsEndDate = statsEndDate;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAvgPurchasePrice() {
        return avgPurchasePrice;
    }

    public BigDecimal getSalesStandardDeviation() {
        return salesStandardDeviation;
    }

    public LocalDate getStatsBeginDate() {
        return statsBeginDate;
    }

    public LocalDate getStatsEndDate() {
        return statsEndDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyStats that = (CurrencyStats) o;
        return Objects.equals(currency, that.currency) &&
                Objects.equals(avgPurchasePrice, that.avgPurchasePrice) &&
                Objects.equals(salesStandardDeviation, that.salesStandardDeviation) &&
                Objects.equals(statsBeginDate, that.statsBeginDate) &&
                Objects.equals(statsEndDate, that.statsEndDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, avgPurchasePrice, salesStandardDeviation, statsBeginDate, statsEndDate);
    }

    @Override
    public String toString() {
        return "CurrencyStats{" +
                "currency='" + currency + '\'' +
                ", avgPurchasePrice=" + avgPurchasePrice +
                ", salesStandardDeviation=" + salesStandardDeviation +
                ", statsBeginDate=" + statsBeginDate +
                ", statsEndDate=" + statsEndDate +
                '}';
    }
}
