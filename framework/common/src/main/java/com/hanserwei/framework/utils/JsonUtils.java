package com.hanserwei.framework.utils;

import com.hanserwei.framework.constant.DateConstants;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author hanserwei
 */
public class JsonUtils {

    private static final JsonMapper JSON_MAPPER;

    static {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateConstants.Y_M_D_H_M_S_FORMAT)));
        simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateConstants.Y_M_D_H_M_S_FORMAT)));

        JSON_MAPPER = JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .addModule(simpleModule)
                .build();
    }

    /**
     * 将给定的对象转换为其JSON字符串表示形式。
     *
     * @param object 要转换为JSON字符串的对象
     * @return 给定对象的JSON字符串表示形式
     */
    public static String toJsonString(Object object) {
        return JSON_MAPPER.writeValueAsString(object);
    }

}
