package com.stock.pycurrent.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.apachecommons.CommonsLog;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CommonsLog
public class JSONUtils {

    private JSONUtils() {
        throw new IllegalStateException("JSONUtils class");
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
    }

    @SuppressWarnings("unused")
    public static ObjectNode getNode() {
        return objectMapper.createObjectNode();
    }


    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(e);
        }
        return "";
    }

    @SuppressWarnings("unused")
    public static String toDoubleJson(Object obj) {
        return toJson(toJson(obj));
    }

    @SuppressWarnings("unused")
    public static String toJson(Object obj, boolean prettyFormat) {
        try {
            String json = objectMapper.writeValueAsString(obj);
            if (prettyFormat) {
                Object jsonObj = objectMapper.readValue(json, Object.class);
                json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj);
            }
            return json;
        } catch (JsonProcessingException e) {
            log.error(e);
        }
        return "";
    }

    @SuppressWarnings("unused")
    public static <T> T fromJson(Class<T> clazz, String json) throws IOException {
        return objectMapper.readValue(json, clazz);
    }

    @SuppressWarnings("unused")
    public static <T> List<T> fromJsonList(Class<T> clazz, String json) throws IOException {
        return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }

    @SuppressWarnings("unused")
    public static <K, V> Map<K, V> fromJsonMap(Class<K> keyClazz, Class<V> valueClazz, String json) throws IOException {
        return objectMapper.readValue(json, objectMapper.getTypeFactory().constructMapType(HashMap.class, keyClazz, valueClazz));
    }

    @SuppressWarnings("unused")
    public static <T> T fromJsonMap(String json, TypeReference<T> typeRef) throws JsonProcessingException {
        return objectMapper.readValue(json, typeRef);
    }

}
