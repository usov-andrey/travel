package org.humanhelper.travel;

import com.codahale.metrics.MetricRegistry;
import org.humanhelper.application.ApplicationConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Андрей
 * @since 28.11.14
 */
@Configuration
@Import({ApplicationConfig.class})
@PropertySource("classpath:application-test.properties")
public class TestConfig {

    @Bean
    public MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }
}
