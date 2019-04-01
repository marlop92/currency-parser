package pl.parser.nbp.model;

import java.util.Objects;

public class CurrencyKey {

    private final String currency;
    private final String valueName;

    public CurrencyKey(String currency, String valueName) {
        this.currency = currency;
        this.valueName = valueName;
    }

    public String getCurrency() {
        return currency;
    }

    public String getValueName() {
        return valueName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyKey that = (CurrencyKey) o;
        return Objects.equals(currency, that.currency) &&
                Objects.equals(valueName, that.valueName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, valueName);
    }

    @Override
    public String toString() {
        return "CurrencyKey{" +
                "currency='" + currency + '\'' +
                ", valueName='" + valueName + '\'' +
                '}';
    }
}
