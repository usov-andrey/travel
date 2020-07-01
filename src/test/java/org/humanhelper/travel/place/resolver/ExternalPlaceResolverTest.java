package org.humanhelper.travel.place.resolver;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.humanhelper.data.dao.Dao;
import org.humanhelper.extservice.http.cache.mapdb.MapDBHttpGetCache;
import org.humanhelper.service.task.TaskRunner;
import org.humanhelper.travel.ApplicationTest;
import org.humanhelper.travel.route.routesearch.RouteService;
import org.humanhelper.travel.route.routesearch.query.RouteQueryValidator;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Андрей
 * @since 30.09.15
 */
@ActiveProfiles(profiles = {TaskRunner.PROFILE, Dao.PROFILE, "memoryDao", MapDBHttpGetCache.PROFILE})
public class ExternalPlaceResolverTest extends ApplicationTest {

    @Autowired
    private RouteService routeService;
    @Autowired
    private RouteQueryValidator routeQueryValidator;

    private void changeLogLevel(Level level) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger("org.humanhelper").setLevel(level);
    }

    @Test
    public void testRegionToRegion() {
		/*
		changeLogLevel(Level.INFO);

		//Place tejakula = placeResolver.getPlace("Tejakula");
		Place kelimutu = placeResolver.getPlace("Kelimutu");
		//Place jokjakarta = placeResolver.getPlace("Yogyakarta");
		Place sub = placeResolver.getPlace("SUB");
		Place bangkok = placeResolver.getPlace("Bangkok");
		//Place derawan = placeResolver.getPlace("Derawan");
		Place berau = placeResolver.getAirportByCode("BEJ");//http://forum.awd.ru/viewtopic.php?t=264857 - 3 часа до острова
		Place bali = placeResolver.getPlace("Kuta");


		Date date = DateHelper.getDate("2015-11-24");
		RouteQuery query = new RouteQuery().currencyUSD().start(bali, DatePeriod.from(date, 2)).stop(kelimutu, 2)
				//.stop(jokjakarta, 1)
				//.stop(sub, 1)
				.stop(berau, 3)
				.end(bangkok, DatePeriod.from(date, 15));
		//RouteQuery query = new RouteQuery().currencyUSD()
		//.startEnd(bangkok, DatePeriod.from(date, 15)).stop(kelimutu, 2)
		//.stop(berau, 3)
		//.stop(bali, 5).stop(jokjakarta, 1);
		//RouteQuery query = new RouteQuery().startEnd(tejakula, DatePeriod.from(date, 10)).stop(kelimutu, 1);
		routeQueryValidator.validate(query);
		RouteSearchResult routeSearchResult = routeService.search(query);
		routeSearchResult.printStatistics(log); */
    }


}
