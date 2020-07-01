package org.humanhelper.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.humanhelper.application.web.SpringEncodeJsonHttpMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * В случае не подключенного JHipster Spring Boot загрузка не используется
 * настраиваем Spring MVC здесь
 *
 * @author Андрей
 * @since 14.11.14
 */
@Configuration
@EnableWebMvc
@Profile(NotHipsterWebConfig.PROFILE)
@PropertySource("classpath:config/application.properties")
public class NotHipsterWebConfig extends WebMvcConfigurerAdapter {

    public static final String PROFILE = "web";

    @Autowired
    private ObjectMapper objectMapper;

    public NotHipsterWebConfig() {
        System.out.print("WebApplicationConfig start");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        //Для Java 7 используется JodaTimeConverter, а для него нужно задавать аннотацию DateTimeFormat для каждого
        //параметра запроса
        //Добавляем конвертер по-умолчанию
        /*
		registry.addConverter(new Converter<String, Date>() {
			@Override
			public Date convert(String source) {
				return DateHelper.isDateWithTime(source) ?
						DateHelper.getDateTime(source) :
						DateHelper.getDate(source);
			}
		});*/
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        //ЧТобы в случае если не находится нужный контроллер дальше обработка шла на стандартный Servler файлов
        configurer.enable();
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer
                .setUseSuffixPatternMatch(false);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //Заменяем objectMapper на свой
        HttpMessageConverter exists = null;
        for (HttpMessageConverter converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                exists = converter;
            }
        }
        converters.remove(exists);
        SpringEncodeJsonHttpMessageConverter jsonHttpMessageConverter = new SpringEncodeJsonHttpMessageConverter();
        jsonHttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(0, jsonHttpMessageConverter);
    }

}
