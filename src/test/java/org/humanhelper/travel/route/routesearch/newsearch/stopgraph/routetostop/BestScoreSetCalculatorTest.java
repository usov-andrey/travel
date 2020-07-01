package org.humanhelper.travel.route.routesearch.newsearch.stopgraph.routetostop;

import org.humanhelper.service.utils.CollectionHelper;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Андрей
 * @since 05.11.15
 */
public class BestScoreSetCalculatorTest {

    @Test
    public void test() {
        Map<String, Map<String, Float>> score = new HashMap<>();
        BestScoreSetCalculator<String, String> calculator = new BestScoreSetCalculator<String, String>() {
            @Override
            protected Float getScore(String key, String forKey) {
                Float value = score.get(key).get(forKey);
                return value;
            }

            @Override
            protected void found(Map<String, String> keys, float price) {
                super.found(keys, price);
                System.out.println("Found:" + price + " " + keys);
            }
        };
        String e11 = "e11";
        String e12 = "e12";

        String e2 = "e2";

        String e31 = "e31";
        String e32 = "e32";

        String e41 = "e41";
        String e42 = "e42";

        CollectionHelper.getHashMapOrCreate(score, e11).putAll(new HashMap<String, Float>() {{
            put(e2, 11f);//11
            put(e31, 12f);
            put(e32, 5f);
            put(e41, 17f);
            put(e42, 22f);
        }});
        CollectionHelper.getHashMapOrCreate(score, e12).putAll(new HashMap<String, Float>() {{
            put(e2, 7f);
            put(e31, 11f);
            put(e32, 20f);
            put(e41, 17f);
            put(e42, 5f);
        }});

        CollectionHelper.getHashMapOrCreate(score, e2).putAll(new HashMap<String, Float>() {{
            put(e11, 6f); //6
            put(e12, 22f);
            put(e31, 14f);
            put(e32, 24f);
            put(e41, 12f);
            put(e42, 17f);
        }});

        CollectionHelper.getHashMapOrCreate(score, e31).putAll(new HashMap<String, Float>() {{
            put(e2, 5f);//5
            put(e11, 10f);
            put(e12, 20f);
            put(e41, 10f);
            put(e42, 5f);
        }});
        CollectionHelper.getHashMapOrCreate(score, e32).putAll(new HashMap<String, Float>() {{
            put(e2, 12f);
            put(e11, 24f);
            put(e12, 50f);
            put(e41, 50f);
            put(e42, 17f);
        }});

        CollectionHelper.getHashMapOrCreate(score, e41).putAll(new HashMap<String, Float>() {{
            put(e2, 17f);
            put(e11, 28f);
            put(e12, 30f);
            put(e31, 13f);
            put(e32, 38f);
        }});
        CollectionHelper.getHashMapOrCreate(score, e42).putAll(new HashMap<String, Float>() {{
            put(e2, 12f);//12
            put(e11, 38f);
            put(e12, 13f);
            put(e31, 21f);
            put(e32, 13f);
        }});


        Map<String, Collection<String>> keys = new HashMap<>();
        keys.put("e1", Arrays.asList(e11, e12));
        keys.put("e2", Arrays.asList(e2));
        keys.put("e3", Arrays.asList(e31, e32));
        keys.put("e4", Arrays.asList(e41, e42));

        calculator.calculateBestPath(keys);
        System.out.println("Best score:" + calculator.getBestScore() + " " + calculator.getBestKeys());
		/*
		Found:35.0 [e11, e2, e31, e41]
		Found:34.0 [e11, e2, e31, e42]
		Found:40.0 [e11, e2, e32, e41]
		Found:35.0 [e11, e2, e32, e42]
		Found:37.0 [e12, e41, e31, e2]
		Found:48.0 [e12, e41, e32, e2]
		Found:36.0 [e12, e42, e2, e31]
		Found:46.0 [e12, e42, e2, e32]
		Best score:34.0 [e11, e2, e31, e42]
		 */
    }

}