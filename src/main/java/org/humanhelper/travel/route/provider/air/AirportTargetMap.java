package org.humanhelper.travel.route.provider.air;

import org.humanhelper.service.utils.CollectionHelper;
import org.humanhelper.travel.place.PlaceService;
import org.humanhelper.travel.place.type.transport.Airport;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Андрей
 * @since 07.10.15
 */
public abstract class AirportTargetMap {

    private Map<Airport, Set<Airport>> targetMap;
    private Supplier<PlaceService> placeResolverSupplier;

    public AirportTargetMap(Supplier<PlaceService> placeResolverSupplier) {
        this.placeResolverSupplier = placeResolverSupplier;
    }

    abstract protected void createTargets();

    public Set<Airport> get(Airport airport) {
        if (targetMap == null) {
            synchronized (this) {
                if (targetMap == null) {
                    targetMap = new HashMap<>();
                    createTargets();
                }
            }
        }
        return CollectionHelper.getHashSetOrCreate(targetMap, airport);
    }

    protected final void addTarget(String sourceCode, String targetCode) {
        Airport source = placeResolverSupplier.get().getAirportByCode(sourceCode);
        if (source == null) {
            throw new IllegalArgumentException("Not found airport with code:" + sourceCode);
        }
        Airport target = placeResolverSupplier.get().getAirportByCode(targetCode);
        if (target == null) {
            throw new IllegalArgumentException("Not found airport with code:" + targetCode);
        }
        CollectionHelper.getHashSetOrCreate(targetMap, source).add(target);
    }

    protected final void addTarget(String code) {
        //Из этого source можно попасть по все другие source и из всех других сюда
        Airport source = placeResolverSupplier.get().getAirportByCode(code);
        if (source == null) {
            throw new IllegalArgumentException("Not found airport with code:" + code);
        }
        for (Airport key : targetMap.keySet()) {
            Set<Airport> targets = targetMap.get(key);
            targets.add(source);
        }
        Set<Airport> targets = new HashSet<>();
        targets.addAll(targetMap.keySet());
        targetMap.put(source, targets);
    }
}
