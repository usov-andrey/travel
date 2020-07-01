package org.humanhelper.travel.route.routesearch.newsearch.score;

import java.util.Comparator;

/**
 * @author Андрей
 * @since 02.11.15
 */
public class PriceWithDiscountScoreEntry<K> {

    private K key;
    private PriceWithDiscountScore score;

    public PriceWithDiscountScoreEntry(K key, PriceWithDiscountScore score) {
        this.key = key;
        this.score = score;
    }

    public static <K> Comparator<PriceWithDiscountScoreEntry<K>> getPriceWithDiscountComparator() {
        return (o1, o2) -> Float.compare(o1.score.priceWithDiscount, o2.score.priceWithDiscount);
    }

    public K getKey() {
        return key;
    }

    public PriceWithDiscountScore getScore() {
        return score;
    }
}
