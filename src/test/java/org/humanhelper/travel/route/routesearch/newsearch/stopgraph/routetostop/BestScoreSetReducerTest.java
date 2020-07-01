package org.humanhelper.travel.route.routesearch.newsearch.stopgraph.routetostop;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.humanhelper.service.utils.CollectionHelper;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * @author Андрей
 * @since 05.11.15
 */
public class BestScoreSetReducerTest {

    @Test
    public void test() {

        Map<String, Map<String, Float>> score = new HashMap<>();

        BestScoreSetReducer<String, String> reducer = new BestScoreSetReducer<String, String>() {
            @Override
            protected Pair<Float, Float> getScorePair(String key1, String key2) {
                if (score.get(key1) == null || score.get(key2) == null) {
                    return null;
                }
                Float price1 = score.get(key1).get(key2);
                Float price2 = score.get(key2).get(key1);
                return (price1 != null && price2 != null) ? new ImmutablePair<>(price1, price2) : null;
            }
        };


        String e11 = "e11";
        String e12 = "e12";
        String e13 = "e13";
        String e14 = "e14";

        String e2 = "e2";

        String e31 = "e31";
        String e32 = "e32";
        String e33 = "e33";

        CollectionHelper.getHashMapOrCreate(score, e11).putAll(new HashMap<String, Float>() {{
            put(e2, 11f);//11
        }});
        CollectionHelper.getHashMapOrCreate(score, e12).putAll(new HashMap<String, Float>() {{
            put(e2, 7f);
            put(e32, 20f);
        }});
        CollectionHelper.getHashMapOrCreate(score, e13).putAll(new HashMap<String, Float>() {{
            put("123", 7f);
            put("234", 20f);
        }});
        //e14 вообще скидок нет

        CollectionHelper.getHashMapOrCreate(score, e2).putAll(new HashMap<String, Float>() {{
            put(e11, 6f); //6
            put(e12, 22f);
            put(e31, 14f);
        }});

        CollectionHelper.getHashMapOrCreate(score, e31).putAll(new HashMap<String, Float>() {{
            put(e2, 5f);//5
        }});
        CollectionHelper.getHashMapOrCreate(score, e32).putAll(new HashMap<String, Float>() {{
            put(e12, 50f);
        }});

        CollectionHelper.getHashMapOrCreate(score, e33).putAll(new HashMap<String, Float>() {{
            put("2323", 17f);
        }});


        Map<String, Collection<String>> keys = new HashMap<>();
        keys.put("e1", Arrays.asList(e11, e12, e13, e14));
        keys.put("e2", Arrays.asList(e2));
        keys.put("e3", Arrays.asList(e31, e32, e33));

        reducer.reduce(keys);
        Map<String, Collection<String>> reducedKeys = reducer.getTargetKeys();
        assertTrue(reducedKeys.get("e1").contains(e11));
        assertTrue(reducedKeys.get("e1").contains(e12));
        assertTrue(!reducedKeys.get("e1").contains(e13));
        assertTrue(!reducedKeys.get("e1").contains(e14));
        assertTrue(reducedKeys.get("e2").contains(e2));
        assertTrue(reducedKeys.get("e3").contains(e31));
        assertTrue(reducedKeys.get("e3").contains(e32));

        BestScoreSetCalculator<String, String> calculator = new BestScoreSetCalculator<String, String>() {
            @Override
            protected Float getScore(String key, String forKey) {
                return reducer.getScore(key, forKey);
            }

            @Override
            protected void found(Map<String, String> keys, float price) {
                super.found(keys, price);
                System.out.println("Found:" + price + " " + keys);
            }
        };
        calculator.calculateBestPath(reducedKeys);
    }

}