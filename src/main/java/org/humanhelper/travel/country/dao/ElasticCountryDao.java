package org.humanhelper.travel.country.dao;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.humanhelper.travel.country.Country;
import org.humanhelper.travel.dao.elastic.AbstractElasticCRUDDao;

import java.io.IOException;
import java.util.Collection;

import static org.elasticsearch.index.query.QueryBuilders.matchPhrasePrefixQuery;

/**
 * @author Андрей
 * @since 08.01.15
 */
public class ElasticCountryDao extends AbstractElasticCRUDDao<Country> implements CountryDao {

    public ElasticCountryDao(Client client) {
        super("countries", "country", Country.class, client);
    }

    @Override
    protected void createMapping(XContentBuilder builder) throws IOException {
		/*
		"_id" : {
            "path" : "post_id"
        }
		 */
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
                startObject(Country.NAME_FIELD).
                field("type", "string").
                field("store", true).
                endObject().
                startObject("currency").
                field("type", "string").
                field("index", "not_analyzed").
                endObject().
                endObject()
        ;
    }

    @Override
    public Collection<Country> getByNamePrefix(String namePrefix, int limitCount) {
        return getCollection(searchRequestBuilder().setQuery(matchPhrasePrefixQuery(Country.NAME_FIELD, namePrefix)), limitCount);
    }
}
