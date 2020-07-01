package org.humanhelper.travel.dataprovider.task;

import org.humanhelper.extservice.task.queue.Task;
import org.humanhelper.service.spring.DependencyInjector;
import org.humanhelper.service.task.TaskRunner;
import org.humanhelper.service.utils.DateHelper;
import org.humanhelper.travel.ApplicationTest;
import org.humanhelper.travel.dao.memory.dataprovider.MemoryFixedPriceDataProvider;
import org.humanhelper.travel.dao.memory.dataprovider.MemoryFloatPriceDataProvider;
import org.humanhelper.travel.dataprovider.DataProvider;
import org.humanhelper.travel.dataprovider.DataProviderStore;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.place.type.transport.BusStation;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.route.type.AnyTimeRoute;
import org.humanhelper.travel.route.type.FixedTimeRoute;
import org.humanhelper.travel.route.usage.UsageDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Тестирование обновление маршрутов согласно приоритетам
 * У нас есть маршруты:
 * Москва-Бангкок(1-го и 2-го января), Москва-Гонконг(1-го и 2-го января),
 * Бангкок-Паттайя(anyTime), Паттайя-Чанг(anyTime)
 * Интересы пользователей: Бангкок, и 2-го января
 * <p>
 * Результат обновления должен быть такой:
 * Москва-Бангкок(2-го) - 14
 * Москва-Бангкок(1-го января) - 13
 * Бангкок-Паттайя - 12
 * Москва-Гонконг(2-го января) - 11
 * Москва-Гонконг(1-го января) - 10
 * Паттайя-Чанг - 9
 */
@ActiveProfiles(profiles = {"memoryDao"})
public class UpdateRoutesTaskTest extends ApplicationTest {

    @Autowired
    private UsageDao usageDao;
    @Autowired
    private DataProviderStore dataProviderStore;
    @Autowired
    private DependencyInjector injector;

    private Airport moscow = Place.build(Airport.class, "Moscow");
    private Airport bangkok = Place.build(Airport.class, "Bangkok");
    private Airport hongkong = Place.build(Airport.class, "Hongkong");
    private BusStation pattaya = Place.build(BusStation.class, "Pattaya");
    private BusStation chang = Place.build(BusStation.class, "Koh Chang");

    private Map<FixedPriceRoutes, Integer> priorityMap = new HashMap<>();

    private TaskRunner taskRunner() {
        return new TaskRunner() {
            public void run(Runnable task, float priority) {
                log.info(task + " priority:" + priority);
                if (task instanceof UpdateRoutesByPlacesDateDataProviderTask) {
                    DayRoutes routes = ((UpdateRoutesByPlacesDateDataProviderTask) task).getDayRoutes();
                    assertEquals(priorityMap.get(routes), new Integer((int) priority));
                } else if (task instanceof UpdateRoutesWithFixedPriceDataProviderTask) {
                    FixedPriceRoutes routes = ((UpdateRoutesWithFixedPriceDataProviderTask) task).getRoutes();
                    assertEquals(priorityMap.get(routes), new Integer((int) priority));
                }
            }
        };
    }

    @Test
    public void testDoRun() throws Exception {
        Date jan1 = DateHelper.setTime(new Date(), 0, 0);
        Date jan2 = DateHelper.incDays(jan1, 1);
        priorityMap.put(new FixedPriceRoutes(bangkok, pattaya), 12);
        priorityMap.put(new FixedPriceRoutes(pattaya, chang), 9);
        priorityMap.put(new DayRoutes(moscow, bangkok, jan1), 13);
        priorityMap.put(new DayRoutes(moscow, bangkok, jan2), 14);
        priorityMap.put(new DayRoutes(moscow, hongkong, jan1), 10);
        priorityMap.put(new DayRoutes(moscow, hongkong, jan2), 11);
        //Удаляем стандартные поставщики
        dataProviderStore.clear();


        MemoryFixedPriceDataProvider fixedPriceDataProvider = new MemoryFixedPriceDataProvider("1");
        prepareDataProvider(fixedPriceDataProvider);
        fixedPriceDataProvider.addRoute(new AnyTimeRoute(getPriceResolver(), bangkok, pattaya, 90, null));
        fixedPriceDataProvider.addRoute(new AnyTimeRoute(getPriceResolver(), pattaya, chang, 60 * 4, null));

        MemoryFloatPriceDataProvider floatPriceDataProvider = new MemoryFloatPriceDataProvider("2");
        prepareDataProvider(floatPriceDataProvider);
        floatPriceDataProvider.setUpdateDays(2);//Обновлять цены только за два дня
        floatPriceDataProvider.addRoute(new FixedTimeRoute(moscow, getPriceResolver(), bangkok, null, jan1, DateHelper.incDays(jan1, 1)));
        floatPriceDataProvider.addRoute(new FixedTimeRoute(moscow, getPriceResolver(), bangkok, null, jan2, DateHelper.incDays(jan2, 1)));
        floatPriceDataProvider.addRoute(new FixedTimeRoute(moscow, getPriceResolver(), hongkong, null, jan1, DateHelper.incDays(jan1, 1)));
        floatPriceDataProvider.addRoute(new FixedTimeRoute(moscow, getPriceResolver(), hongkong, null, jan2, DateHelper.incDays(jan2, 1)));
        usageDao.addUsage(jan1);
        usageDao.addUsage(bangkok);

        UpdateRoutesByAllProvidersTask task = Task.create(UpdateRoutesByAllProvidersTask.class);
        task.doRun();

    }

    private void prepareDataProvider(DataProvider dataProvider) throws Exception {
        injector.inject(dataProvider);
        dataProvider.afterPropertiesSet();
        dataProvider.setTaskRunner(taskRunner());
    }

    private PriceResolver getPriceResolver() {
        return new PriceResolver(0, null, null);
    }

}