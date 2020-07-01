package org.humanhelper.travel.route.provider;

import org.humanhelper.service.singleton.Singleton;
import org.humanhelper.service.utils.CollectionHelper;
import org.humanhelper.travel.place.Place;

import java.util.Map;
import java.util.Set;

/**
 * Наполняем сетку маршрутов Targets один раз и храним ее в виде targets
 *
 * @author Андрей
 * @since 05.10.15
 */
public abstract class AbstractTargetsRouteProvider extends AbstractRouteProvider {

    private Singleton<Map<Place, Set<Place>>> targets = new Singleton<>(this::createTargets);

    @Override
    public Set<Place> getTargets(Place source) {
        return CollectionHelper.getSafeHashSet(targets.get(), source);
    }

    protected abstract Map<Place, Set<Place>> createTargets();
}
