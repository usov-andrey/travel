package org.humanhelper.travel.route.routesearch.newsearch.stopgraph;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.Activity;
import org.humanhelper.travel.route.routesearch.newsearch.stopgraph.routetostop.SingleRouteToStopActivity;
import org.humanhelper.travel.route.type.StopActivity;
import org.slf4j.Logger;

import java.util.*;

/**
 * Хранит для каждой остановки(Activity) map мест, куда можно из нее
 * попасть и для каждого места задается Activity куда можно попасть и путь
 * <p>
 * Так как нам в дальнейшем необходимо обходить граф согласно какому-то пути, то для быстродействия
 * для каждой StopActivityVertex мы храним следующие вершины, через группировку по Place
 * <p>
 * В случае если мы пришли в какой-то TargetPlace и до этого мы уже сюда приходили, то
 * если мы ранее приходили позже, то создаем новый список Collection<StopActivityVertex> - нам незачем долго путешествовать
 * Если мы уже были в этой остановке, то добавляем новый маршрут для этой остановки(оптимальный он или нет, решается в
 * stopActivityVertex.addPriceRoute)
 * Если мы ранее приходили раньше, то данный маршрут игнорируем(return false)
 *
 * @author Андрей
 * @since 29.10.15
 */
public class StopActivityMap {

    private int count = 0;
    private int stepCount = 0;
    private Map<Activity, Map<Place, Collection<StopActivityVertex>>> map = new HashMap<>();

    public boolean add(StopActivity start, StopActivity target, SingleRouteToStopActivity routeToTarget) {
        count++;
        stepCount++;
        Map<Place, Collection<StopActivityVertex>> targetMap = map.computeIfAbsent(start, (start1) -> new HashMap<>());
        Collection<StopActivityVertex> stopActivityVertexes = targetMap.computeIfAbsent(target.getSource(), (target1) -> new ArrayList<>());

        //Если в списке уже есть вершины, то если мы пришли уже в какую-то существующую, то нужно добавить маршрут
        for (StopActivityVertex stopActivityVertex : stopActivityVertexes) {
            StopActivity stopActivity = stopActivityVertex.getActivity();
            if (stopActivity.equals(target)) {
                return stopActivityVertex.addPriceRoute(routeToTarget);
            }
            //Если мы пришли в target ранее чем приходили до этого, то все предыдущие прохождения нам не нужны
            long targetStartTime = target.getStartTime().getTime();
            long existStartTime = stopActivity.getStartTime().getTime();
            if (targetStartTime < existStartTime) {
                //Заменяем список вершин
                List<StopActivityVertex> newStopActivityVertexes = new ArrayList<>();
                newStopActivityVertexes.add(new StopActivityVertex(target, routeToTarget));
                targetMap.put(target.getSource(), newStopActivityVertexes);
                return true;
            } else if (targetStartTime > existStartTime) {
                //Значит мы пришли в более позднее место, сохранять нам такой переход не нужно
                return false;
            }
        }
        //Значит в списке нет target, а есть другие с такой же датой прихода, но с другим количеством ночей для остановки
        stopActivityVertexes.add(new StopActivityVertex(target, routeToTarget));
        return true;
    }

    /**
     * Activity для места targetPlace куда можно попасть из source
     */
    public Collection<StopActivityVertex> getTarget(Activity source, Place targetPlace) {
        Map<Place, Collection<StopActivityVertex>> targetMap = map.get(source);
        if (targetMap != null) {
            return targetMap.getOrDefault(targetPlace, Collections.emptySet());
        }
        return Collections.emptySet();
    }

    public int getCount() {
        return count;
    }

    public void log(Logger log) {
        log.debug("Routes between stops processed:" + count);
        int edgesCount = 0;
        for (Map<Place, Collection<StopActivityVertex>> targetMap : map.values()) {
            for (Collection<StopActivityVertex> vertexes : targetMap.values()) {
                edgesCount = edgesCount + vertexes.size();
            }
        }
        log.debug("StopActivityGraph edgesCount:" + edgesCount);
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }
}
