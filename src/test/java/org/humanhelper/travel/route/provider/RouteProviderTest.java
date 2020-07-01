package org.humanhelper.travel.route.provider;

import org.humanhelper.data.dao.Dao;
import org.humanhelper.service.spring.DependencyInjector;
import org.humanhelper.service.task.TaskRunner;
import org.humanhelper.travel.ApplicationTest;
import org.humanhelper.travel.dao.memory.MemoryDaoFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Андрей
 * @since 08.10.15
 */
@ActiveProfiles(profiles = {TaskRunner.PROFILE, Dao.PROFILE, MemoryDaoFactory.PROFILE})
public abstract class RouteProviderTest<T extends RouteProvider> extends ApplicationTest implements InitializingBean {

    protected T routeProvider;
    @Autowired
    private DependencyInjector dependencyInjector;

    @Override
    public void afterPropertiesSet() throws Exception {
        routeProvider = dependencyInjector.inject(createRouteProvider());
    }

    abstract protected T createRouteProvider();


}