package org.humanhelper.travel.price.currency;

import org.humanhelper.travel.ApplicationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Currency;

import static org.junit.Assert.assertEquals;

public class CurrencyConverterTest extends ApplicationTest {

    private final Currency eur = Currency.getInstance("EUR");
    private final Currency usd = Currency.getInstance("USD");
    @Autowired
    private CurrencyConverter currencyConverter;

    @Test
    public void testConvert() throws Exception {
        float amountUSD = 100;
        float amountEUR = currencyConverter.convert(amountUSD, usd, eur);
        assertEquals(amountUSD, currencyConverter.convert(amountEUR, eur, usd), 0.01f);
    }
}