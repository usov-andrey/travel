package org.humanhelper.travel.price.currency;

import org.humanhelper.service.conversion.ConverterService;
import org.humanhelper.service.http.Http;
import org.humanhelper.service.utils.DateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Андрей
 * @since 08.01.15
 */
@Service
public class CurrencyConverter {

    private static final Logger log = LoggerFactory.getLogger(CurrencyConverter.class);

    @Autowired
    private ConverterService converterService;

    private String url = "http://rate-exchange.appspot.com/currency?from=%s&to=%s";
    private int expiredInMinutes = 60;

    private Map<Integer, CurrencyRate> rates = new HashMap<>();

    public float convert(float amount, Currency from, Currency to) {
        int hashCode = from.hashCode() * 31 + to.hashCode();
        CurrencyRate rate = rates.get(hashCode);
        if (rate == null || DateHelper.minutesBetweenDates(rate.getUpdateTime(), new Date()) > expiredInMinutes) {
            if (log.isDebugEnabled()) {
                log.debug("Updating currency rate from " + from + " to " + to);
            }
            rate = converterService.getJSONObject(Http.get(
                    String.format(url, from.getCurrencyCode(), to.getCurrencyCode())).responseBody(), CurrencyRate.class);
            rate.setUpdateTime(new Date());
            rates.put(hashCode, rate);
        }
        return amount * rate.getRate();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setExpiredInMinutes(int expiredInMinutes) {
        this.expiredInMinutes = expiredInMinutes;
    }
}
