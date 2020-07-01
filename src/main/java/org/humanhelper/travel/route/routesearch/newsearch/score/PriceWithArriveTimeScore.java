package org.humanhelper.travel.route.routesearch.newsearch.score;

/**
 * Добавленр время прибытия
 *
 * @author Андрей
 * @since 07.11.15
 */
public class PriceWithArriveTimeScore extends PriceScore {

    private long arriveTime;

    public PriceWithArriveTimeScore(float price, long arriveTime) {
        super(price);
        this.arriveTime = arriveTime;
    }

    public long getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(long arriveTime) {
        this.arriveTime = arriveTime;
    }

    /**
     * Сохраняет максимальное значение
     */
    public void saveBigger(float price, long arriveTime) {
        super.saveBigger(price);
        if (arriveTime > this.arriveTime) {
            this.arriveTime = arriveTime;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PriceWithArriveTimeScore)) return false;
        if (!super.equals(o)) return false;

        PriceWithArriveTimeScore that = (PriceWithArriveTimeScore) o;

        return arriveTime == that.arriveTime;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (arriveTime ^ (arriveTime >>> 32));
        return result;
    }
}
