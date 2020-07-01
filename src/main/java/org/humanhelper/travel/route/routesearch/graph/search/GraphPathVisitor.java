package org.humanhelper.travel.route.routesearch.graph.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Андрей
 * @since 27.10.15
 */
public interface GraphPathVisitor<T, P extends Path<T>> {

    Logger log = LoggerFactory.getLogger(GraphPathVisitor.class);

    /**
     * @return not null, если продолжать дальше идти от этой вершины
     */
    P visit(T vertex, P path);

    /**
     * @return not null, если продолжать идти от стартовой вершины
     */
    P start(T vertex);
}
