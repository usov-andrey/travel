package org.humanhelper.travel.place.type.region;

/**
 * @author Андрей
 * @since 18.09.15
 */
public class Island extends Region {

    public static Island build(String name) {
        return build(Island.class, name);
    }
}
