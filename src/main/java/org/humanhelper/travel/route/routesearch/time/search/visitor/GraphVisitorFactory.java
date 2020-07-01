package org.humanhelper.travel.route.routesearch.time.search.visitor;

import org.humanhelper.service.spring.DelegateFactory;
import org.humanhelper.travel.route.routesearch.time.Graph;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * @author Андрей
 * @since 04.10.15
 */
@Service
public class GraphVisitorFactory extends DelegateFactory<Graph, GraphVisitor> {

    @Override
    public Function<Graph, GraphVisitor> createDefaultImplementation() {
        return BreadFirstGraphVisitor::new;
    }

}
