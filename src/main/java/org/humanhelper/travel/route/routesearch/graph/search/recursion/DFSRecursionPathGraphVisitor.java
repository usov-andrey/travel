package org.humanhelper.travel.route.routesearch.graph.search.recursion;

import org.humanhelper.travel.route.routesearch.graph.search.Path;

/**
 * @author Андрей
 * @since 29.10.15
 */
public abstract class DFSRecursionPathGraphVisitor<V, P> extends DFSRecursionGraphVisitor<V> {

    protected Path<P> path;

    @Override
    public void start(DFSRecursionGraphSearch<V> search, V vertex) {
        this.path = Path.empty();
        super.start(search, vertex);
    }

    /**
     * Посетить вершины далее с новым путем
     */
    protected final void visitWithAddToPath(V vertex, Path<P> newPath) {
        Path<P> oldPath = path;
        path = newPath;
        visitWithoutAddToPath(vertex);
        path = oldPath;
    }

    protected final void visitWithoutAddToPath(V vertex) {
        search.searchInTargets(vertex);
    }
}