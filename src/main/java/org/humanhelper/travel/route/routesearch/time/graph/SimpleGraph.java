package org.humanhelper.travel.route.routesearch.time.graph;

import org.humanhelper.travel.route.routesearch.time.Graph;
import org.humanhelper.travel.route.routesearch.time.search.vertex.Vertex;

import java.util.*;

/**
 * @author Андрей
 * @since 02.12.14
 */
public class SimpleGraph implements Graph {

    private int edgesCount;
    private Map<Vertex, Collection<Vertex>> graph;
    private Map<Vertex, Collection<Vertex>> forFastIterate;//Возможно для скорости и стоит использовать отдельный пул с быстрым итератором

    public SimpleGraph() {
        super();
        graph = new HashMap<>();
        forFastIterate = new HashMap<>();
        edgesCount = 0;
    }

    public void addVertex(Vertex routeItem) {
        if (!graph.containsKey(routeItem)) {
            graph.put(routeItem, new LinkedHashSet<Vertex>());
            //forFastIterate.put(routeItem, new ArrayList<RouteItem>());
            //Нужно использовать что-то, что сохраняет порядок добавления(иначе работать не будет - в Route есть оптимизация)
            //Также нужно использовать, что-то из Set, так как не нужно добавлять ребра дубликаты
        }
    }

    public void addEdge(Vertex source, Vertex target) {
        Collection<Vertex> targets = graph.get(source);
        if (targets.add(target)) {
            edgesCount++;
        }
    }

    @Override
    public void optimize() {
        for (Vertex item : graph.keySet()) {
            Collection<Vertex> targets = graph.get(item);
            List<Vertex> targetList = new ArrayList<>(targets);
            //При обходе важен порядок, если обходим сначала одно место, то это будет быстрее
            //TODO
			/*
			Collections.sort(targetList, new Comparator<Vertex>() {
				@Override
				public int compare(Vertex o1, Vertex o2) {
					int value = o1.compareTo(o2);
					return value;
				}
			});
			RouteItem:
			if (routeItem instanceof AbstractWayRouteItem) {
			return ((AbstractWayRouteItem) routeItem).getTarget().getId().compareTo(source.getId());
		}
		return source.getId().compareTo(routeItem.getSource().getId());
		AbstractRouteItem:
		if (routeItem instanceof AbstractWayRouteItem) {
			return getTarget().getId().compareTo(((AbstractWayRouteItem) routeItem).getTarget().getId());
		} else return getTarget().getId().compareTo(routeItem.getSource().getId());

			*/

            forFastIterate.put(item, targetList);
        }
        graph.clear();
    }

    @Override
    public Collection<Vertex> getTargets(Vertex item) {
        //return graph.get(item);
        return forFastIterate.get(item);
    }

    @Override
    public String toString() {
        return "Vertexs:" + forFastIterate.keySet().size() + " Edges:" + edgesCount;
    }
}
