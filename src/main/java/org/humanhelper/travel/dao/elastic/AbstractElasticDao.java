package org.humanhelper.travel.dao.elastic;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.humanhelper.service.conversion.ConverterService;
import org.humanhelper.service.conversion.JsonViews;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.elasticsearch.client.Requests.deleteIndexRequest;
import static org.elasticsearch.client.Requests.refreshRequest;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @author Андрей
 * @since 14.01.15
 */
public abstract class AbstractElasticDao<T> implements InitializingBean {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected ConverterService converterService;
    protected Client client;

    protected String index;
    protected String type;
    protected Class<T> beanClass;

    public AbstractElasticDao(String index, String type, Class<T> beanClass, Client client) {
        log.debug("Creating dao:" + this);
        this.index = index;
        this.type = type;
        this.beanClass = beanClass;
        this.client = client;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //Если индекса еще нет, то создаем его
        if (!client.admin().indices().prepareExists(index).execute().actionGet().isExists()) {
            createIndex();
        }
    }

    abstract protected void createMapping(XContentBuilder builder) throws IOException;

    protected T getObject(String json) {
        if (json == null) {
            return null;
        }
        return converterService.getJSONObject(json, beanClass, JsonViews.Db.class);
    }


    protected XContentBuilder createBuilder() throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder().startObject().startObject(type);
        createMapping(builder);
        builder.endObject().endObject();
        return builder;
    }

    protected void createIndex() {
        try {
            XContentBuilder builder = createBuilder();
            CreateIndexRequestBuilder requestBuilder = client.admin().indices().prepareCreate(index);
            if (builder != null) {
                requestBuilder.addMapping(type, builder);
            }
            requestBuilder.execute().actionGet();
        } catch (Exception e) {
            throw new IllegalStateException("Error on create index:" + index + " type:" + type, e);
        }
    }

    @RequestMapping(value = "reindex", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteIndex() {
        client.admin().indices().delete(deleteIndexRequest(index)).actionGet();
        createIndex();
        client.admin().indices().refresh(refreshRequest(index));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<T> getAll() {
        return getScrolledCollection(searchRequestBuilder());
    }

    public Collection<T> get(Collection<String> idSet) {
        return getScrolledCollection(searchRequestBuilder(idsQuery(type).addIds(idSet.toArray(new String[idSet.size()]))));
    }

    protected <V extends T> List<V> getCollection(SearchRequestBuilder searchRequestBuilder, int size) {
        List<V> result = new ArrayList<>();
        searchRequestBuilder.setSize(size);
        //log.debug("Get collection:" + searchRequestBuilder);
        SearchResponse response = searchRequestBuilder.execute().actionGet();
        handleSearchResponseHits(response, result);
        //log.debug("Result count:"+result.size());
        return result;
    }

    /**
     * Возвращает все элементы в коллекции
     */
    protected <V> List<V> getScrolledCollection(SearchRequestBuilder searchRequestBuilder) {
        List<V> result = new ArrayList<>();
        searchRequestBuilder.setScroll(new TimeValue(60000)).setSize(100);
        //log.debug("Get scrolled collection:" + searchRequestBuilder);
        SearchResponse response = searchRequestBuilder.execute().actionGet();
        //Scroll until no hits are returned
        while (true) {
            handleSearchResponseHits(response, result);
            response = client.prepareSearchScroll(response.getScrollId()).setScroll(new TimeValue(600000)).execute().actionGet();
            //Break condition: No hits are returned
            if (response.getHits().getHits().length == 0) {
                break;
            }
        }
        //log.debug("Result count:"+result.size());
        return result;
    }

    protected <V> void handleSearchResponseHits(SearchResponse response, List<V> result) {
        for (SearchHit searchHit : response.getHits()) {
            V object = (V) getObject(searchHit.getSourceAsString());
            result.add(object);
        }
    }


    protected <V extends T> V getOne(SearchRequestBuilder searchRequestBuilder) {
        List<V> result = getCollection(searchRequestBuilder, 1);
        return result.isEmpty() ? null : result.get(0);
    }

    protected SearchRequestBuilder searchRequestBuilder() {
        return client.prepareSearch(index);
    }

    protected SearchRequestBuilder searchRequestBuilder(QueryBuilder queryBuilder) {
        return searchRequestBuilder().setQuery(queryBuilder);
    }

    protected SearchRequestBuilder searchRequestBuilder(FilterBuilder filterBuilder) {
        return searchRequestBuilder().setQuery(filteredQuery(matchAllQuery(), filterBuilder));
    }

    @RequestMapping(value = "clear", method = RequestMethod.GET)//сделано для тестов
    @ResponseStatus(value = HttpStatus.OK)
    public void delete() {
        client.prepareDeleteByQuery(index)
                .setQuery(QueryBuilders.matchAllQuery())
                .execute()
                .actionGet();
    }

    @RequestMapping(value = "count", method = RequestMethod.GET)
    public
    @ResponseBody
    long getCount() {
        CountResponse response = client.prepareCount(index)
                .setQuery(termQuery("_type", type))
                .execute()
                .actionGet();
        return response.getCount();
    }
}
