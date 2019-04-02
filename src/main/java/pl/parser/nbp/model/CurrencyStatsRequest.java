package pl.parser.nbp.model;

import java.time.LocalDate;
import java.util.Objects;

public class CurrencyStatsRequest {

    private String currencyCode;
    private LocalDate statsBeginDate;
    private LocalDate statsEndDate;

    public CurrencyStatsRequest(String currencyCode, LocalDate statsBeginDate, LocalDate statsEndDate) {
        this.currencyCode = currencyCode;
        this.statsBeginDate = statsBeginDate;
        this.statsEndDate = statsEndDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
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
        CurrencyStatsRequest request = (CurrencyStatsRequest) o;
        return Objects.equals(currencyCode, request.currencyCode) &&
                Objects.equals(statsBeginDate, request.statsBeginDate) &&
                Objects.equals(statsEndDate, request.statsEndDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyCode, statsBeginDate, statsEndDate);
    }

    @Override
    public String toString() {
        return "CurrencyStatsRequest{" +
                "currencyCode='" + currencyCode + '\'' +
                ", statsBeginDate=" + statsBeginDate +
                ", statsEndDate=" + statsEndDate +
                '}';
    }
}
