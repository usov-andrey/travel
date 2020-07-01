package org.humanhelper.travel.transport;

import org.humanhelper.data.dao.CRUDDao;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Андрей
 * @since 14.11.14
 */
@RequestMapping("transport")
public interface TransportDao extends CRUDDao<Transport, String> {

}
