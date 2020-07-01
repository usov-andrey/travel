package org.humanhelper.application;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import org.humanhelper.service.ServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Есть профили запуска:
 * RMI: httpRMI, asyncRMI
 * DAO: titanDao, memoryDao(для тестов), remoteDao(отправлять данные на удаленный сервер)
 * WEB: web(для инициализации web приложения)
 * TASK: syncTask(выполнение задачи в одном потоке), asyncTask(выполнение задач через очередь)
 * QUEUE: memoryQueue, remoteHttpQueue(обращение к удаленной очереди по http), redissonQueue(очередь в redis)
 * Для подключения работы с redisson, нужно использовать профиль redis
 * Для то, чтобы приложение обрабатывало входящие asyncRMI запросы необходимо использовать профиль asyncRMIRerver
 *
 * @author Андрей
 * @since 13.11.14
 */
@Configuration
@EnableCaching
@Import({ServiceConfig.class})
@ComponentScan(basePackages = {"org.humanhelper.travel", "org.humanhelper.application"})
public class ApplicationConfig {

    @Autowired
    private MetricRegistry metrics;

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }

    @Bean
    public ConsoleReporter reporter() {
        return ConsoleReporter
                .forRegistry(metrics)
                .build();
    }

}
