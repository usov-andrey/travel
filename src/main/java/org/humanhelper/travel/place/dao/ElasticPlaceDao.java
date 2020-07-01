package org.humanhelper.travel.place.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.humanhelper.data.bean.json.BeanWithIdModule;
import org.humanhelper.data.bean.proxy.IdBeanProxyFactory;
import org.humanhelper.service.conversion.JsonViews;
import org.humanhelper.travel.country.Country;
import org.humanhelper.travel.country.dao.ElasticCountryDao;
import org.humanhelper.travel.dao.elastic.AbstractElasticCRUDDao;
import org.humanhelper.travel.geo.Location;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.createdb.PlaceKind;
import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.route.provider.external.TPRouteProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.Collection;
import java.util.TimeZone;

import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.matchPhrasePrefixQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;

/**
 * place.country храним как place.countryId и при загрузке используем lazy
 * place.region храним как place.regionId и при загрузке используем lazy
 * place.targets храним как place.targetsId и при загрузке используем lazy
 * если place это регион, то region.childs храним как region.childsId и при загрузке используем lazy
 *
 * @author Андрей
 * @since 29.12.14
 */
public class ElasticPlaceDao extends AbstractElasticCRUDDao<Place> implements PlaceDao {

    @Autowired
    private ElasticCountryDao countryDao;
    @Autowired
    private TPRouteProvider tpRouteProvider;

    private ObjectMapper objectMapper;

    public ElasticPlaceDao(Client client) {
        super("places", "place", Place.class, client);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        objectMapper = createObjectMapper();
    }

    /**
     * Используем специальную сериализацию в json
     */
    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setTimeZone(TimeZone.getDefault());
        BeanWithIdModule module = new BeanWithIdModule();

        module
                //Все ссылки на страны храним в виде id страны, загружаем страну из
                .addClass(Country.class, getProxyFactory(countryDao))
                //В поле регион храним только id
                .addField(Place.REGION_FIELD, new IdBeanProxyFactory<>(Region.class, id -> {
                    if (log.isDebugEnabled()) {
                        log.debug("Lazy loading object Region by id:" + id);
                    }
                    return getById(id);
                }))
                //В полях childs и targets храним список идентификаторов
                .addCollectionField(Place.TARGETS_FIELD, Place.class, getProxyFactory(this))
                .addCollectionField(Region.CHILDS_FIELD, Place.class, getProxyFactory(this));
        objectMapper.registerModule(module);
        return objectMapper;
    }

    @Override
    public Collection<Place> getByNamePrefix(String namePrefix, int limitCount) {
        return getCollection(searchRequestBuilder().setQuery(matchPhrasePrefixQuery(Place.NAME_FIELD, namePrefix)), limitCount);
    }

    @Override
    public Collection<Region> getPopularRegions(Country country, int count) {
        return getCollection(searchRequestBuilder(boolFilter().must(termFilter(Place.COUNTRY_FIELD, country.getId())))
                .addSort(Region.HOTELS_FIELD, SortOrder.DESC), count);
    }

    @Override
    public <T extends Place> T get(String name, Country country, Class<T> placeClass) {
        return getOne(searchRequestBuilder(boolFilter().must(termFilter(Place.COUNTRY_FIELD, country.getId()))
                        .must(termFilter(Place.KIND_FIELD, PlaceKind.getName(placeClass)))
                        .must(queryFilter(matchPhraseQuery(Place.NAME_FIELD, name)))
                )
        );
    }

    @Override
    public <T extends Place> Collection<T> getAll(Class<T> placeClass) {
        return getScrolledCollection(searchRequestBuilder(boolFilter()
                .must(termFilter(Place.KIND_FIELD, PlaceKind.getName(placeClass))))
        );
    }

    @Override
    public <T extends Place> Collection<T> getByCountry(Class<T> placeClass, Country country) {
        return getScrolledCollection(searchRequestBuilder(boolFilter()
                        .must(termFilter(Place.KIND_FIELD, PlaceKind.getName(placeClass)))
                        .must(termFilter(Place.COUNTRY_FIELD, country.getId()))
                )
        );
    }

    @Override
    public <T extends Place> Collection<T> getByRegion(Region region, Class<T> placeClass) {
        return getScrolledCollection(searchRequestBuilder(boolFilter()
                        .must(termFilter(Place.KIND_FIELD, PlaceKind.getName(placeClass)))
                        .must(termFilter(Place.COUNTRY_FIELD, region.getCountry()))
                        .must(termFilter(Place.REGION_FIELD, region.getId()))
                )
        );
    }

    @Override
    public <T extends Place> T getById(String id) {
        return (T) get(id);
    }

    @Override
    public <T extends Place> Collection<T> getInRadius(Class<T> placeClass, Country country, Location location, double radiusInKm) {
        return getScrolledCollection(searchRequestBuilder(boolFilter().must(termFilter(Place.KIND_FIELD, PlaceKind.region))
                .must(termFilter(Place.COUNTRY_FIELD, country.getId()))
                .must(geoDistanceFilter(Location.LOCATION_FIELD).distance(radiusInKm, DistanceUnit.KILOMETERS).point(location.getLat(), location.getLng()))
        ));
    }

    @Override
    public Collection<Place> getInRadius(Location location, double radiusInKm) {
        return getScrolledCollection(searchRequestBuilder(boolFilter()
                .must(geoDistanceFilter(Location.LOCATION_FIELD).distance(radiusInKm, DistanceUnit.KILOMETERS).point(location.getLat(), location.getLng()))
        ));
    }

    @Override
    public Airport getAirportByCode(String code) {
        //Храним код аэропорта в виде id
        Place place = get(code);
        try {
            return Airport.get(place);
        } catch (Exception e) {
            throw new IllegalStateException("By code " + code + " found not airport:" + place);
        }
    }

    @Override
    public Place insert(@RequestBody Place object) {
        if (object.getId() == null) {
            throw new IllegalStateException("Not found place id:" + object);
        }
        //Необходимо заполнить targets
        if (Airport.isAirport(object)) {
            object.createTargets(tpRouteProvider.getTargets(Airport.get(object)));
        }

        return super.insert(object);
    }

    @Override
    public void addRoad(Place source, Place target) {
		/*
		if (source.addTarget(target)) {
			if (log.isDebugEnabled()) {
				log.debug("Add road from " + source + " to " + target);
			}
			update(source.getId(), source);
		}*/
        //TODO
    }

    @Override
    protected void createMapping(XContentBuilder builder) throws IOException {
        builder.
                startObject("_id").
                field("path", "id").
                endObject().
                startObject("properties").
                startObject("id").
                field("type", "string").
                field("store", true).
                field("index", "not_analyzed").
                endObject().
                startObject(Place.KIND_FIELD).
                field("type", "string").
                field("store", true).
                field("index", "not_analyzed").
                endObject().
                startObject(Place.NAME_FIELD).
                field("type", "string").
                field("store", true).
                endObject().
                startObject(Location.LOCATION_FIELD).
                field("type", "geo_point").
                field("geohash", "true").
                endObject().
                startObject(Place.REGION_FIELD).
                field("type", "string").
                field("store", true).
                field("index", "not_analyzed").
                endObject().
                startObject(Place.COUNTRY_FIELD).
                field("type", "string").
                field("store", true).
                field("index", "not_analyzed").
                endObject().
                endObject()
        ;
    }

    //Конвертация в JSON


    @Override
    protected String getJsonObject(Place object) {
        return converterService.objectToJSONString(objectMapper, object, JsonViews.Db.class);
    }

    @Override
    protected Place getObject(String json) {
        if (json == null) {
            return null;
        }
        return converterService.getJSONObject(objectMapper, json, beanClass, JsonViews.Db.class);
    }
}
