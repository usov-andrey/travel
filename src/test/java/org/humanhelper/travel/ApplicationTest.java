package org.humanhelper.travel;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Андрей
 * @since 28.11.14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public abstract class ApplicationTest {

    protected static final Logger log = LoggerFactory.getLogger(ApplicationTest.class);

}
