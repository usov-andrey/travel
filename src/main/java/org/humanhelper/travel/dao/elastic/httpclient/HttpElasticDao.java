package org.humanhelper.travel.dao.elastic.httpclient;

import io.searchbox.action.Action;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.*;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.IndicesExists;
import org.elasticsearch.index.query.QueryBuilders;
import org.humanhelper.data.bean.id.Id;
import org.humanhelper.data.dao.CRUDDao;
import org.humanhelper.service.conversion.ConverterService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

/**
 * @author Андрей
 * @since 29.12.14
 */
public class HttpElasticDao<T extends Id<String>> implements CRUDDao<T, String>, InitializingBean {

    @Autowired
    private ConverterService converterService;

    @Autowired
    private JestClient client;

    private String index;
    private String type;
    private Class<T> beanClass;

    public HttpElasticDao(String index, String type, Class<T> beanClass) {
        this.index = index;
        this.type = type;
        this.beanClass = beanClass;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //Если индекса еще нет, то создаем его
        if (!client.execute(new IndicesExists.Builder(index).build()).isSucceeded()) {
            client.execute(new CreateIndex.Builder(index).build());
        }
    }

    @Override
    public Collection<T> getAll() {
        SearchResult result = execute(new Search.Builder("").build());
        return result.getSourceAsObjectList(beanClass);
    }

    @Override
    public Collection<T> get(Collection<String> idSet) {
        return null;  //TODO
    }

    @Override
    public T get(@PathVariable("id") String id) {
        Get get = new Get.Builder(index, id).type(type).build();
        JestResult result = execute(get);
        String object = result.getJsonString();
        if (object.contains("\"found\":false")) {
            return null;
        } else {
            return converterService.getJSONObject(object, beanClass);
        }
    }

    @Override
    public T insert(@RequestBody T object) {
        Index index = new Index.Builder(converterService.objectToJSONString(object)).index(this.index).type(type).id(object.getId()).build();
        execute(index);
        return object;
    }

    @Override
    public T update(@PathVariable("id") String id, @RequestBody T object) {
        execute(new Update.Builder(converterService.objectToJSONString(object)).index(index).type(type).id(id).build());
        return object;
    }

    @Override
    public T delete(@PathVariable("id") String id) {
        T place = get(id);
        execute(new Delete.Builder(id).index(index).type(type).build());
        return place;
    }

    @Override
    public void delete() {
        execute(new DeleteByQuery.Builder(QueryBuilders.matchAllQuery().toString()).build());
    }

    @Override
    public long getCount() {
        return Double.doubleToLongBits(execute(new Count.Builder().build()).getCount());
    }

    private <R extends JestResult> R execute(Action<R> clientRequest) {
        try {
            return client.execute(clientRequest);
        } catch (Exception e) {
            throw new RuntimeException("Error on execute elastic request:" + clientRequest, e);
        }
    }
}
