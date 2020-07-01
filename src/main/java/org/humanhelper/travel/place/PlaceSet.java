package org.humanhelper.travel.place;

import java.util.*;

/**
 * Введено небольшое кэширование списка идентфиикаторов мест
 *
 * @author Андрей
 * @since 16.09.15
 */
public class PlaceSet<T extends Place> extends HashSet<T> {

    private Set<String> idSet;

    public PlaceSet() {
    }

    public PlaceSet(T place) {
        add(place);
    }

    public Set<String> getIdSet() {
        if (idSet == null) {
            idSet = new HashSet<>();
            for (Place place : this) {
                idSet.add(place.getId());
            }
        }
        return idSet;
    }

    public Map<String, Place> createMap() {
        Map<String, Place> map = new HashMap<>();
        for (Place place : this) {
            map.put(place.getId(), place);
        }
        return map;
    }

    @Override
    public boolean add(T place) {
        idSet = null;
        return super.add(place);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        idSet = null;
        return super.addAll(c);
    }

    @Override
    public boolean remove(Object o) {
        idSet = null;
        return super.remove(o);
    }

    @Override
    public void clear() {
        idSet = null;
        super.clear();
    }
}
