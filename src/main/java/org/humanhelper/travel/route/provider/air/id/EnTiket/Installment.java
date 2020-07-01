package org.humanhelper.travel.route.provider.air.id.EnTiket;

/**
 * {"currency":"IDR","tenor_plan":3,"min_payment":500000,"base_price":960157,"pay_monthly":336463,"pay_monthly_string":"IDR 336.463"}
 *
 * @author Андрей
 * @since 16.10.15
 */
public class Installment {

    private String currency;
    private Integer tenor_plan;
    private Integer min_payment;
    private Integer base_price;
    private Integer pay_monthly;
    private String pay_monthly_string;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getTenor_plan() {
        return tenor_plan;
    }

    public void setTenor_plan(Integer tenor_plan) {
        this.tenor_plan = tenor_plan;
    }

    public Integer getMin_payment() {
        return min_payment;
    }

    public void setMin_payment(Integer min_payment) {
        this.min_payment = min_payment;
    }

    public Integer getBase_price() {
        return base_price;
    }

    public void setBase_price(Integer base_price) {
        this.base_price = base_price;
    }

    public Integer getPay_monthly() {
        return pay_monthly;
    }

    public void setPay_monthly(Integer pay_monthly) {
        this.pay_monthly = pay_monthly;
    }

    public String getPay_monthly_string() {
        return pay_monthly_string;
    }

    public void setPay_monthly_string(String pay_monthly_string) {
        this.pay_monthly_string = pay_monthly_string;
    }
}
