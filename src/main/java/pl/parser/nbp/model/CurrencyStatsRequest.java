package pl.parser.nbp.model;

import java.time.LocalDate;

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
}
