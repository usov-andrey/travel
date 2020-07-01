package org.humanhelper.travel.price.currency;

import java.util.Date;

/**
 * @author Андрей
 * @since 08.01.15
 */
public class CurrencyRate {

    private String from;
    private String to;
    private float rate;
    private Date updateTime;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
