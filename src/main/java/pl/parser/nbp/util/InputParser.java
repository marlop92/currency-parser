package pl.parser.nbp.util;

import pl.parser.nbp.exceptions.InvalidInputException;
import pl.parser.nbp.model.CurrencyStatsRequest;

import java.time.LocalDate;
import java.util.List;

public class InputParser {

    private final static List<String> VALID_CURRENCIES = List.of("USD", "EUR", "CHF", "GBP");
    private static final String INVALID_CURRENCY_CODE = "Currency %s is not a valid currency. Valid currencies are : ";
    private static final String INVALID_ARGUMENT_NUMBER = "Invalid number of arguments. Example input: USD 2019-01-01 2019-01-06";

    public static CurrencyStatsRequest parseInput(String[] args) {
        if(args.length < 3 || args.length > 5) {
            throw new InvalidInputException(INVALID_ARGUMENT_NUMBER);
        }
        String currencyCode = args[0];
        if (!VALID_CURRENCIES.contains(currencyCode)) {
            throw new InvalidInputException(String.format(INVALID_CURRENCY_CODE, currencyCode) +
                    VALID_CURRENCIES.toString());
        }
        LocalDate beginDate = LocalDate.parse(args[1]);
        LocalDate endDate = LocalDate.parse(args[2]);

        if(args.length >= 4) {
            GlobalConfig.retryAttemptInterval = Integer.valueOf(args[3]);
        }

        if(args.length >= 5) {
            GlobalConfig.retryMaxAttempts = Integer.valueOf(args[4]);
        }

        return new CurrencyStatsRequest(currencyCode, beginDate, endDate);
    }
}
