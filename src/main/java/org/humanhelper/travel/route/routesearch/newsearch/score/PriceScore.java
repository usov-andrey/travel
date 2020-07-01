package org.humanhelper.travel.route.routesearch.newsearch.score;

/**
 * @author Андрей
 * @since 07.11.15
 */
public class PriceScore {

    protected float price;

    public PriceScore(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void saveBigger(float price) {
        if (price > this.price) {
            this.price = price;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PriceScore)) return false;

        PriceScore that = (PriceScore) o;

        return Float.compare(that.price, price) == 0;

    }

    @Override
    public int hashCode() {
        return (price != +0.0f ? Float.floatToIntBits(price) : 0);
    }
}
