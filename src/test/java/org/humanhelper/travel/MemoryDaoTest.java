package org.humanhelper.travel;

import org.humanhelper.data.dao.Dao;
import org.humanhelper.service.task.TaskRunner;
import org.humanhelper.travel.dao.memory.MemoryDaoFactory;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Андрей
 * @since 08.01.16
 */
@ActiveProfiles({TaskRunner.PROFILE, Dao.PROFILE, MemoryDaoFactory.PROFILE})
public class MemoryDaoTest extends ApplicationTest {
}
