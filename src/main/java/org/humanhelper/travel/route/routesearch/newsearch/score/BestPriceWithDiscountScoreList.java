package org.humanhelper.travel.route.routesearch.newsearch.score;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Храним только обьекты, у которых цена равна minPrice или priceWithDiscount меньше minPrice
 *
 * @author Андрей
 * @since 02.11.15
 */
public class BestPriceWithDiscountScoreList<K> {

    protected Map<K, PriceWithDiscountScore> scoreMap;
    protected PriceWithDiscountScore minScore;
    private List<K> sortedKeys;//кэгируем ключи отосртированные в порядке возврастания цены со скидкой
    private K minPriceKey;

    public BestPriceWithDiscountScoreList(PriceWithDiscountScore minScore) {
        scoreMap = new ConcurrentHashMap<>();
        this.minScore = new PriceWithDiscountScore(minScore);
    }

    public BestPriceWithDiscountScoreList(K key, PriceWithDiscountScore score) {
        this(score);
        scoreMap.put(key, score);
        minPriceKey = key;
    }

    public PriceWithDiscountScore getScore(K key) {
        return scoreMap.get(key);
    }

    /**
     * @return данная оценка будет добавлена, если ее цена со скидкой меньше minPrice
     */
    public boolean canAdd(PriceWithDiscountScore score) {
        return score.priceWithDiscount < minScore.price;
    }

    public boolean add(K key, PriceWithDiscountScore score) {
        float price2 = score.getPrice();
        float discountPrice2 = score.getPriceWithDiscount();
        boolean isAdd = false;
        float minPrice = minScore.price;
        //Изменяем если нужно minScore
        score.updateScore(minScore);
        if (price2 <= minPrice) {
            minPrice = price2;
            minPriceKey = key;
            //Изменилась минимальная стоимость, возможно другие цены нам уже не нужны
            //Удаляем маршруты, у которых цена со скидкой больше чем minPrice
            for (Map.Entry<K, PriceWithDiscountScore> entry : scoreMap.entrySet()) {
                if (entry.getValue().priceWithDiscount > minPrice) {
                    scoreMap.remove(entry.getKey());
                }
            }
            isAdd = true;
        } else if (discountPrice2 <= minPrice) {
            isAdd = true;
        }
        if (isAdd) {
            if (scoreMap.put(key, score) != null) {
                throw new IllegalStateException("Key already exists:" + key + " in map:" + scoreMap);
            }
        }
        return isAdd;
    }

    public Set<K> getKeys() {
        return scoreMap.keySet();
    }

    public int size() {
        return scoreMap.size();
    }

    public PriceWithDiscountScore getMinScore() {
        return minScore;
    }

    public K getMinPriceKey() {
        return minPriceKey;
    }

    /**
     * @return ключ в порядке возврастания priceWithDiscount
     */
    public List<K> getSortedKeys() {
        if (sortedKeys == null) {
            sortedKeys = new ArrayList<>();
            List<PriceWithDiscountScoreEntry<K>> list = new ArrayList<>();
            for (Map.Entry<K, PriceWithDiscountScore> entry : scoreMap.entrySet()) {
                list.add(new PriceWithDiscountScoreEntry<>(entry.getKey(), entry.getValue()));
            }
            //Сортируем в порядке увеличения минимальной стоимости
            Collections.sort(list, PriceWithDiscountScoreEntry.getPriceWithDiscountComparator());
            for (PriceWithDiscountScoreEntry<K> entry : list) {
                sortedKeys.add(entry.getKey());
            }
        }
        return sortedKeys;
    }

    @Override
    public String toString() {
        return "scoreMap=" + scoreMap +
                ", minScore=" + minScore;
    }
}
