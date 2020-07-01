package org.humanhelper.travel.place;

import org.humanhelper.travel.place.type.region.Region;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Операции со списком разных наследников Place
 *
 * @author Андрей
 * @since 17.02.15
 */
public class PlaceList extends AbstractPlaceList<Place> {

    public static String simple(Collection<Place> places) {
        StringBuilder sb = new StringBuilder();
        for (Place place : places) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(place.nameOrId());
        }
        return sb.toString();
    }

    /**
     * Ищем среди дочерних мест тоже
     */
    public Place findWithChilds(Predicate<Place> predicate) {
        return findWithChilds(this, predicate);
    }

    private Place findWithChilds(Collection<Place> places, Predicate<Place> predicate) {
        for (Place place : places) {
            if (predicate.test(place)) {
                return place;
            }
            if (Region.isRegion(place)) {
                Place childPlace = findWithChilds(Region.get(place).getChilds(), predicate);
                if (childPlace != null) {
                    return childPlace;
                }
            }
        }
        return null;
    }

    public void processWithChilds(Consumer<Place> processor) {
        processWithChilds(this, processor, place -> true);
    }

    /**
     * @param processor      обработчик каждого элемента
     * @param childPredicate определяет, нужно обрабатывать дочерние элементы или нет
     */
    public void processWithChilds(Consumer<Place> processor, Predicate<Place> childPredicate) {
        processWithChilds(this, processor, childPredicate);
    }

    private void processWithChilds(Collection<Place> places, Consumer<Place> processor, Predicate<Place> childPredicate) {
        for (Place place : places) {
            processor.accept(place);
            if (Region.isRegion(place) && childPredicate.test(place)) {
                processWithChilds(Region.get(place).getChilds(), processor, childPredicate);
            }
        }
    }

    /**
     * Ищем регион по имени, если текущее место регион, то смотрим его childs тоже
     * Также имя сравниваем со всеми переводами
     */
    public Region findRegionByName(String name) {
        return getRegion(this, name);
    }

    private Region getRegion(Collection<Place> places, String name) {
        for (Place place : places) {
            if (Region.isRegion(place)) {
                if (place.equalsNameWithTranslations(name)) {
                    return Region.get(place);
                }
                Region childRegion = getRegion(Region.get(place).getChilds(), name);
                if (childRegion != null) {
                    return childRegion;
                }
            }
        }
        return null;
    }

    /*
        Добавляем место, если место - регион, то добавляем и все дочерние места
     */
    public void addWithChilds(Place place) {
        add(place);
        if (Region.isRegion(place)) {
            for (Place child : Region.get(place).getChilds()) {
                addWithChilds(child);
            }
        }
    }

    public Set<Place> toSet() {
        Set<Place> places = new HashSet<>();
        places.addAll(this);
        return places;
    }

    /**
     * Находим родительский регион, чей boundingBox включает place.location
     */
    public Region getParentRegionByBBox(Place place) {
        for (Place child : this) {
            if (Region.isRegion(child)) {
                Region groupRegion = Region.get(child);
                if (!groupRegion.bBoxIsEmpty() && groupRegion.getbBox().inside(place.getLocation())) {
                    return groupRegion;
                }
            }
        }
        return null;
    }

    /**
     * В модели между Place возможны циклы, у Place задан region, а в region.childs находится это же место
     * Для перевода этой структуры в json необходимо преобразование
     * Для этого убираем у всех Place значение поля Region, если этот Region есть в этом списке,
     * также убираем это место из списка, так как оно будет сериализовано через region.childs
     *
     * @param result - результирующий список для вывода
     */
    public void saveForJson(PlaceList result) {
        //Добавляем в результирующий список элемент, только если у него пустой регион
        //Если задан регион, то добавляем регион
        for (Place place : this) {
            Region region = place.getRegion();
            if (region == null) {
                result.add(place);
            } else if (!result.contains(region)) {
                result.add(region);
            }
        }
        //Если текущее место - это регион, то нужно пробежаться по всем childs и очистить в них поле регион
        for (Place place : result) {
            clearRegion(place);
        }
    }

    private void clearRegion(Place place) {
        if (Region.isRegion(place)) {
            for (Place child : Region.get(place).getChilds()) {
                child.setRegion(null);
                clearRegion(child);
            }
        }
    }

    //При чтении из Json, нужно если это место Region, то для всех childs нужно проставить этот region
    public void loadFromJson() {
        this.forEach(this::loadPlace);
    }

    private void loadPlace(Place place) {
        if (Region.isRegion(place)) {
            Region region = Region.get(place);
            for (Place child : region.getChilds()) {
                child.setRegion(region);
                loadPlace(child);
            }
        }
    }

}
