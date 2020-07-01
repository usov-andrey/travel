package org.humanhelper.travel.route.routesearch.time;

import org.humanhelper.travel.route.routesearch.time.search.vertex.Vertex;

import java.util.Collection;

/**
 * Граф
 *
 * @author Андрей
 * @since 05.12.14
 */
public interface Graph {

    public void addVertex(Vertex vertex);

    public void addEdge(Vertex source, Vertex target);

    public Collection<Vertex> getTargets(Vertex source);

    //Оптимизация графа перед поиском
    public void optimize();
}
