package org.humanhelper.travel.route.routesearch.graph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Андрей
 * @since 22.10.15
 */
public interface GraphSearch<V> {

    Logger log = LoggerFactory.getLogger(GraphSearch.class);

    void search(V vertex);
}
