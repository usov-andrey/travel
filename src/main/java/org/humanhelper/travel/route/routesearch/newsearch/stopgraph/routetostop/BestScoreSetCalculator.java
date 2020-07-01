package org.humanhelper.travel.route.routesearch.newsearch.stopgraph.routetostop;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Есть функция, которая сохраняет score для ключа
 * Также есть функция, которая сохраняет score для пары ключей
 * Необходимо определить лучший набор пар с наибольшим score
 * Перебираем варианты по алгоритму:
 * исходный набор:1,2,3
 * варианты:1-2,3 1-3,2 1,2-3
 * <p>
 * исходный набор:1,2,3,4
 * варианты:1-2,3-4 1-3,2-4 1-4,2-3
 * <p>
 * исходный набор:1,2,3,4,5
 * варианты:1-2,3-4,5 1-3,2-4,5 1-4,2-3,5 1-5,2-3,4 1,2-3,4-5 1,2-4,3-5 1,2-5,3-4
 *
 * @author Андрей
 * @since 04.11.15
 */
public abstract class BestScoreSetCalculator<KSupplier, K> {

    protected float bestScore;
    protected Map<KSupplier, K> bestKeys;

    protected abstract Float getScore(K key, K forKey);

    private float calculatePrice(Collection<K> keys) {
        //minScore = Sum(minScoreK1, minScoreK2, ..., minScoreKm)
        float score = 0;
        for (K key : keys) {
            score += minScore(key, keys);
        }
        return score;
    }

    public Map<KSupplier, K> getBestKeys() {
        return bestKeys;
    }

    public float getBestScore() {
        return bestScore;
    }

    private float minScore(K key, Collection<K> keys) {
        //minScoreK1 = Min(f(K1, K2), f(K1, K3), ... f(K1, Km))
        float minScore = Float.MAX_VALUE;
        for (K Km : keys) {
            if (!key.equals(Km)) {
                Float score = getScore(key, Km);
                if (score == null) {
                    continue;
                }
                if (score < minScore) {
                    minScore = score;
                }
            }
        }
        return minScore;
    }

    private void found(Map<KSupplier, K> keys) {
        float price = calculatePrice(keys.values());
        found(keys, price);
    }

    protected void found(Map<KSupplier, K> keys, float price) {
        if (bestScore > price) {
            bestScore = price;
            bestKeys = new HashMap<>();
            bestKeys.putAll(keys);
        }
    }

    private void calculateBestPath(Map<KSupplier, Collection<K>> sourceKeys, Map<KSupplier, K> keys) {
        if (sourceKeys.isEmpty()) {
            found(keys);
        } else {
            KSupplier kSupplier = sourceKeys.keySet().iterator().next();
            Collection<K> value = sourceKeys.remove(kSupplier);
            for (K key : value) {
                keys.put(kSupplier, key);
                calculateBestPath(sourceKeys, keys);
                keys.remove(kSupplier);
            }
            sourceKeys.put(kSupplier, value);
        }
    }

    public void calculateBestPath(Map<KSupplier, Collection<K>> sourceKeys) {
        bestScore = Float.MAX_VALUE;
        calculateBestPath(sourceKeys, new HashMap<>());
    }

}
