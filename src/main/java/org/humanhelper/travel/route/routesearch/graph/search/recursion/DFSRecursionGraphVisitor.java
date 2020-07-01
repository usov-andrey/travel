package org.humanhelper.travel.route.routesearch.graph.search.recursion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Андрей
 * @since 29.10.15
 */
public abstract class DFSRecursionGraphVisitor<V> {

    protected static Logger log = LoggerFactory.getLogger(DFSRecursionGraphVisitor.class);

    protected DFSRecursionGraphSearch<V> search;

    public void start(DFSRecursionGraphSearch<V> search, V vertex) {
        setSearch(search);
        visit(vertex);
    }

    protected void setSearch(DFSRecursionGraphSearch<V> search) {
        this.search = search;
    }

    abstract public void visit(V vertex);
}
