package org.humanhelper.application.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.humanhelper.service.http.encoder.HttpEncoder;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Реализация поддержки HttpEncoder для того, чтобы можно было шифровать тело запроса и ответа
 *
 * @author Андрей
 * @since 25.05.14
 */
public class SpringEncodeJsonHttpMessageConverter extends MappingJackson2HttpMessageConverter {

    private ObjectMapper objectMapper;
    private HttpEncoder httpEncoder = new HttpEncoder();

    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {

        JavaType javaType = getJavaType(type, contextClass);
        return readJavaType(javaType, inputMessage);
    }

    private Object readJavaType(JavaType javaType, HttpInputMessage inputMessage) {
        try {
            return this.objectMapper.readValue(httpEncoder.getDecodedInputStream(inputMessage.getBody()), javaType);
        } catch (IOException ex) {
            throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        JavaType javaType = getJavaType(clazz, null);
        return readJavaType(javaType, inputMessage);
    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        try {
            String value = this.objectMapper.writeValueAsString(object);
            httpEncoder.encodeResponseToOutputStream(value, outputMessage.getBody());
        } catch (JsonProcessingException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

    public void setHttpEncoder(HttpEncoder httpEncoder) {
        this.httpEncoder = httpEncoder;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        super.setObjectMapper(objectMapper);
        this.objectMapper = objectMapper;
    }
}
