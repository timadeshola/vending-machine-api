package com.timadeshola.vendingmachine.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.timadeshola.vendingmachine.core.AppConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 5:21 PM
 */
@Slf4j
public class AppUtil {

    private static ObjectMapper mapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new Jdk8Module());
        return mapper;
    }

    public static String toJson(Type type) {
        try {
            return mapper().writeValueAsString(type);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error occurred serializing object to json string, error => " + e.getMessage());
        }
    }

    public static <T> String toJson(T t) {
        try {
            return mapper().writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error occurred serializing object to json string, error => " + e.getMessage());
        }
    }


    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper().readValue(json, clazz);
        } catch (IOException e) {
            log.error("Error occurred deserializing object to json string >> {}", e.getMessage());
        }
        return null;
    }

    public static <T, K, V> T fromJson(Map<K, V> json, Class<T> clazz) {
        return mapper().convertValue(json, clazz);
    }

    public static <T, K, V> T fromJson(Map<K, V> json, TypeReference<T> clazz) {
        return mapper().convertValue(json, clazz);
    }

    public static <T> T fromJson(T json, TypeReference<T> clazz) {
        return mapper().convertValue(json, clazz);
    }

    public static <T> List<T> readList(String str, Class<T> type) {
        return readList(str, ArrayList.class, type);
    }

    private static <T> List<T> readList(String str, Class<? extends Collection> type, Class<T> elementType) {
        try {
            return mapper().readValue(str, mapper().getTypeFactory().constructCollectionType(type, elementType));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "system";
        }
        return authentication.getName();
    }

    public static String getParameter(String parameter) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        return request.getParameter(parameter);
    }


    public static Date parseDateUtil(String dateStr) {
        if (dateStr == null) {
            return new Date();
        }
        try {
            return new Date(AppConstant.DateFormatters.dateFormat.parse(dateStr).getTime());
        } catch (ParseException e) {
            log.error("error occurred converting string to date, please check you date format >> {}", e.getMessage());
        }
        return null;
    }

    public static boolean verifyAmount(BigDecimal amount) {
        List<BigDecimal> values = List.of(BigDecimal.valueOf(5), BigDecimal.valueOf(10),
                BigDecimal.valueOf(20), BigDecimal.valueOf(50), BigDecimal.valueOf(100));

        if (values.contains(amount)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(verifyAmount(BigDecimal.valueOf(10)));
    }
}