package org.humanhelper.travel.dao.elastic;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.humanhelper.data.bean.id.Id;
import org.humanhelper.data.bean.id.ProxyIdBean;
import org.humanhelper.data.bean.proxy.IdBeanProxyFactory;
import org.humanhelper.data.dao.CRUDDao;
import org.humanhelper.service.conversion.JsonViews;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Операции на обычной сущностью у которой есть id (BeanWithId)
 *
 * @author Андрей
 * @since 29.12.14
 */
public abstract class AbstractElasticCRUDDao<T extends Id<String>> extends AbstractElasticDao<T> implements CRUDDao<T, String> {

    public AbstractElasticCRUDDao(String index, String type, Class<T> beanClass, Client client) {
        super(index, type, beanClass, client);
    }

    /**
     * ProxyFactory, которую можно подключать для lazy загрузки данных этого типа T
     */
    protected static <V extends ProxyIdBean> IdBeanProxyFactory<V> getProxyFactory(AbstractElasticCRUDDao<V> dao) {
        return new IdBeanProxyFactory<>(dao.beanClass, id -> {
            if (dao.log.isDebugEnabled()) {
                dao.log.debug("Lazy loading object " + dao.beanClass + " by id:" + id);
            }
            return dao.get(id);
        });
    }

    @Override
    public T get(@PathVariable("id") String id) {
        GetResponse response = client.prepareGet(index, type, id).execute().actionGet();
        return getObject(response.getSourceAsString());
    }

    @Override
    public T insert(@RequestBody T object) {
        if (log.isDebugEnabled()) {
            log.debug("Insert object:" + object);
        }
        String json = getJsonObject(object);
        try {
            client.prepareIndex(index, type, object.getId())
                    .setSource(json).execute().actionGet();
            return object;
        } catch (Exception e) {
            throw new IllegalStateException("Error on insert json:" + json, e);
        }
    }

    @Override
    public T update(@PathVariable("id") String id, @RequestBody T object) {
        if (log.isDebugEnabled()) {
            log.debug("Update object:" + object);
        }
        String json = getJsonObject(object);
        try {
            client.prepareUpdate(index, type, id).setDoc(json)
                    .execute().actionGet();
            return object;
        } catch (Exception e) {
            throw new IllegalStateException("Error on update json:" + json, e);
        }
    }

    protected String getJsonObject(T object) {
        return converterService.objectToJSONString(object, JsonViews.Db.class);
    }

    public Collection<T> getById(Collection<T> set) {
        Set<String> idSet = new HashSet<>();
        for (T object : set) {
            idSet.add(object.getId());
        }
        return get(idSet);
    }

    @Override
    public T delete(@PathVariable("id") String id) {
        T place = get(id);
        if (log.isDebugEnabled()) {
            log.debug("Delete object:" + id + " type:" + type);
        }
        client.prepareDelete(index, type, id).execute().actionGet();
        return place;
    }

}
