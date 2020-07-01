package org.humanhelper.travel.dao.mongodb;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.humanhelper.data.bean.id.Id;
import org.humanhelper.data.dao.CRUDDao;
import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Андрей
 * @since 29.12.14
 */
public class MongoDBDao<T extends Id<String>> implements CRUDDao<T, String> {

    @Autowired
    private MongoClient client;

    private Class<T> beanClass;
    private String collectionName;

    public MongoDBDao(Class<T> beanClass, String collectionName) {
        this.beanClass = beanClass;
        this.collectionName = collectionName;
    }

    @Override
    public Collection<T> getAll() {
        Collection<T> result = new ArrayList<>();
        DBCursor<T> cursor = collection().find();
        try {
            while (cursor.hasNext()) {
                result.add(cursor.next());
            }
        } finally {
            cursor.close();
        }
        return result;
    }

    private JacksonDBCollection<T, String> collection() {
        DB db = client.getDB("travel");
        DBCollection dbCollection = db.getCollection(collectionName);
        return JacksonDBCollection.wrap(dbCollection, beanClass, String.class);
    }

    @Override
    public T get(@PathVariable("id") String id) {
        return collection().findOneById(id);
    }

    @Override
    public T insert(@RequestBody T object) {
        WriteResult<T, String> result = collection().insert(object);
        object.setId(result.getSavedId());
        return object;
    }

    @Override
    public T update(@PathVariable("id") String id, @RequestBody T object) {
        collection().updateById(id, object);
        return object;
    }

    @Override
    public Collection<T> get(Collection<String> idSet) {
        return null;  //TODO
    }

    @Override
    public T delete(@PathVariable("id") String id) {
        T object = get(id);
        collection().removeById(id);
        return object;
    }

    @Override
    public void delete() {
        collection().drop();
    }

    @Override
    public long getCount() {
        return collection().getCount();
    }
}
