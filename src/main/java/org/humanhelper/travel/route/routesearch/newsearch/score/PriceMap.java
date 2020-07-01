package org.humanhelper.travel.route.routesearch.newsearch.score;

import java.util.HashMap;

/**
 * @author Андрей
 * @since 27.10.15
 */
public class PriceMap<V> extends HashMap<V, Float> {


    public float getScore(V source) {
        return getOrDefault(source, Float.MAX_VALUE);
    }

    public void setScore(V target, float score) {
        put(target, score);
    }

    public void setStartScore(V start) {
        put(start, 0f);
    }

    /**
     * @return true, если новая цена лучше. В этом случае старая цена в хранилище заменяется новой
     */
    public boolean betterPrice(V source, V target, float sourceTargetPrice) {
        //Перемещаемся если раньше мы сюда еще не приходили с оптимальной ценой
        float score = getScore(source) + sourceTargetPrice;
        //Если targetScore < minSource
        if (score < getScore(target)) {
            setScore(target, score);
            return true;
        }
        return false;
    }

    /**
     * Если уже задано значение, то сохраняет если текущее меньше уже существующего
     */
    public boolean setMin(V key, float value) {
        Float oldValue = get(key);
        if (oldValue == null || oldValue > value) {
            put(key, value);
            return true;
        }
        return false;
    }

}
