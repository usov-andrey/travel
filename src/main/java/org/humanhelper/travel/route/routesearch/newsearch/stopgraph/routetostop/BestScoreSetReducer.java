package org.humanhelper.travel.route.routesearch.newsearch.stopgraph.routetostop;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * @author Андрей
 * @since 05.11.15
 */
public abstract class BestScoreSetReducer<KSupplier, K> {

    private Map<KSupplier, Collection<K>> targetKeys = new HashMap<>();
    private Map<Pair<K, K>, Float> scoreMap = new HashMap<>();

    /**
     * Возвращает null, если между key1 и key2 нет скидки
     */
    protected abstract Pair<Float, Float> getScorePair(K key1, K key2);

    public Float getScore(K key1, K key2) {
        return scoreMap.get(code(key1, key2));
    }

    public Map<KSupplier, Collection<K>> getTargetKeys() {
        return targetKeys;
    }

    protected void saveScore(K key1, K key2, Float score) {
        scoreMap.put(code(key1, key2), score);
    }

    protected Pair<K, K> code(K key, K key2) {
        return new ImmutablePair<>(key, key2);
        //return key.hashCode() ^ key2.hashCode();
    }

    private void saveKey(K key, KSupplier supplier) {
        targetKeys.computeIfAbsent(supplier, (k) -> new HashSet<>()).add(key);
    }

    private void saveScore(K key1, KSupplier supplier1, Collection<K> keys2, KSupplier supplier2) {
        boolean haveScore = false;
        for (K key2 : keys2) {
            Pair<Float, Float> score = getScorePair(key1, key2);
            if (score != null) {
                saveKey(key2, supplier2);
                saveScore(key1, key2, score.getLeft());
                saveScore(key2, key1, score.getRight());
                haveScore = true;
            }
        }
        if (haveScore) {
            saveKey(key1, supplier1);
        }
    }

    protected void reduce(Map<KSupplier, Collection<K>> sourceKeys) {
        Iterator<KSupplier> iterator = sourceKeys.keySet().iterator();
        if (iterator.hasNext()) {
            KSupplier supplier1 = iterator.next();
            Collection<K> keys1 = sourceKeys.get(supplier1);
            while (iterator.hasNext()) {
                KSupplier supplier2 = iterator.next();
                Collection<K> keys2 = sourceKeys.get(supplier2);
                for (K key1 : keys1) {
                    saveScore(key1, supplier1, keys2, supplier2);
                }
            }
            sourceKeys.remove(supplier1);
            reduce(sourceKeys);
        }
    }

}
