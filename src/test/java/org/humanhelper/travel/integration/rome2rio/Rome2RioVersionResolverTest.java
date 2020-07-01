package org.humanhelper.travel.integration.rome2rio;

import org.humanhelper.service.utils.StringHelper;
import org.humanhelper.travel.ApplicationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

/**
 * @author Андрей
 * @since 09.10.15
 */
public class Rome2RioVersionResolverTest extends ApplicationTest {

    @Autowired
    private Rome2RioVersionResolver versionResolver;

    @Test
    public void testGetVersion() throws Exception {
        String version = versionResolver.getVersion();
        assertEquals(version, StringHelper.getFirstNumber(version));
    }
}