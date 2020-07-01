package org.humanhelper.travel.route.routesearch.newsearch.score;

import java.util.Map;

/**
 * Данная оценка хранит список связок price, priceWithDiscount
 * <p>
 * Текущая оценка меньше, если или price или priceWithDiscount меньше
 *
 * @author Андрей
 * @since 31.10.15
 */
public class PriceWithDiscountScore extends PriceScore {

    protected float priceWithDiscount;

    public PriceWithDiscountScore() {
        super(0);
        priceWithDiscount = 0;
    }

    public PriceWithDiscountScore(PriceWithDiscountScore score) {
        super(score.price);
        this.priceWithDiscount = score.priceWithDiscount;
    }

    public PriceWithDiscountScore(float price) {
        super(price);
        this.priceWithDiscount = price;
    }

    public PriceWithDiscountScore(float price, float priceWithDiscount) {
        super(price);
        this.priceWithDiscount = priceWithDiscount;
    }


    /**
     * для score заполняет price и priceWithDiscount минимальными значениями из score и текущего
     * возвращает true, если такое изменение было сделано
     */
    public boolean updateScore(PriceWithDiscountScore score) {
        //Выбираем минимальную цену
        if (score.price > price) {
            score.price = price;
            return true;
        }
        if (score.priceWithDiscount > priceWithDiscount) {
            score.priceWithDiscount = priceWithDiscount;
            return true;
        }
        return false;
    }

    /**
     * Устанавливает для этого ключа минимальную цену и минимальную цену со скидкой
     *
     * @return true, если у старой оценки хуже или цена или цена со скидкой
     */
    public <K> boolean putToMap(Map<K, PriceWithDiscountScore> map, K key) {
        PriceWithDiscountScore oldScore = map.get(key);
        if (oldScore == null) {
            map.put(key, this);
            return true;
        } else {
            return updateScore(oldScore);
        }
    }

    public float getPriceWithDiscount() {
        return priceWithDiscount;
    }

    public void setPriceWithDiscount(float priceWithDiscount) {
        this.priceWithDiscount = priceWithDiscount;
    }

    public void addPrice(float price, float priceWithDiscount) {
        this.price += price;
        this.priceWithDiscount += priceWithDiscount;
    }

    public void addPrice(PriceWithDiscountScore score) {
        this.price += score.price;
        this.priceWithDiscount += score.priceWithDiscount;
    }

    public void decPrice(PriceWithDiscountScore score) {
        this.price -= score.price;
        this.priceWithDiscount -= score.priceWithDiscount;
    }

    public void decPrice(float price, float priceWithDiscount) {
        this.price -= price;
        this.priceWithDiscount -= priceWithDiscount;
    }

    /**
     * @return price + priceWIthDiscount
     */
    public float getPriceSum() {
        return price + priceWithDiscount;
    }

    @Override
    public String toString() {
        return "price=" + price +
                ", priceWithDiscount=" + priceWithDiscount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PriceWithDiscountScore)) return false;
        if (!super.equals(o)) return false;

        PriceWithDiscountScore score = (PriceWithDiscountScore) o;

        return Float.compare(score.priceWithDiscount, priceWithDiscount) == 0;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (priceWithDiscount != +0.0f ? Float.floatToIntBits(priceWithDiscount) : 0);
        return result;
    }
}
