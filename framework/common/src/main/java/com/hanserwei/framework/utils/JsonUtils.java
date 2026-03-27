package com.hanserwei.framework.utils;

import tools.jackson.databind.json.JsonMapper;

/**
 * @author hanserwei
 */
public class JsonUtils {

    private static JsonMapper JSON_MAPPER;

    /**
     * 将给定的对象转换为其JSON字符串表示形式。
     *
     * @param object 要转换为JSON字符串的对象
     * @return 给定对象的JSON字符串表示形式
     */
    public static String toJsonString(Object object) {
        return JSON_MAPPER.writeValueAsString(object);
    }

    /**
     * 初始化JsonMapper确保全局使用这个JsonMapper
     *
     * @param jsonMapper JsonMapper
     */
    public static void init(JsonMapper jsonMapper) {
        JSON_MAPPER = jsonMapper;
    }

}
