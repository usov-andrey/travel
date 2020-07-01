package org.humanhelper.travel.dao.memory;

import org.humanhelper.data.bean.id.Id;
import org.humanhelper.data.dao.CRUDDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author Андрей
 * @since 08.01.15
 */
public class MemoryCRUDDao<T extends Id<String>> implements CRUDDao<T, String> {

    protected static final Logger log = LoggerFactory.getLogger(MemoryCRUDDao.class);

    protected Map<String, T> values = new HashMap<>();

    @Override
    public Collection<T> getAll() {
        return values.values();
    }

    @Override
    public T get(@PathVariable("id") String id) {
        return values.get(id);
    }

    protected void generateId(T object) {
        object.setId(Integer.toString(object.hashCode()));
    }

    @Override
    public T insert(@RequestBody T object) {
        String id = object.getId();
        if (id == null) {
            generateId(object);
        }
        if (!values.containsKey(id)) {
            log.debug("Insert object:" + object);
            values.put(id, object);
        }
        return object;
    }

    @Override
    public Collection<T> get(Collection<String> idSet) {
        Collection<T> objects = new HashSet<>();
        for (String id : idSet) {
            if (values.containsKey(idSet)) {
                objects.add(get(id));
            }
        }
        return objects;
    }

    @Override
    public T update(@PathVariable("id") String id, @RequestBody T object) {
        log.debug("Update object:" + object);
        values.put(id, object);
        return object;
    }

    @Override
    public T delete(@PathVariable("id") String id) {
        log.debug("Delete object by id:" + id);
        return values.remove(id);
    }

    @Override
    public void delete() {
        log.debug("Clear");
        values.clear();
    }

    @Override
    public long getCount() {
        return values.size();
    }
}
