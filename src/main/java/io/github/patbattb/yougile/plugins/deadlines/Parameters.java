package io.github.patbattb.yougile.plugins.deadlines;

import io.github.patbattb.plugins.core.expection.PluginCriticalException;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Parameters {

    private final String token;
    private final String dailyColumId;
    private final String weeklyColumnId;
    private final int requestCountPerMinute;

    private final String tokenFiledName = "token";
    private final String dailyColumnIdFieldName = "column.daily.id";
    private final String weeklyColumnIdFieldName = "column.weekly.id";
    private final String requestFrequencyFieldName = "request.frequency";

    Parameters(Path configFile) throws PluginCriticalException {
        Properties properties = new Properties();
        try {
            properties.load(Files.newBufferedReader(configFile, StandardCharsets.UTF_8));
            token = getStringProperty(properties, tokenFiledName);
            dailyColumId = getStringProperty(properties, dailyColumnIdFieldName);
            weeklyColumnId = getStringProperty(properties, weeklyColumnIdFieldName);
            requestCountPerMinute = getPositiveIntProperty(properties, requestFrequencyFieldName);
        } catch (IOException e) {
            throw new PluginCriticalException("Can't read config file.", e);
        }
    }

    String getToken() {
        return token;
    }

    String getDailyColumId() {
        return dailyColumId;
    }

    String getWeeklyColumnId() {
        return weeklyColumnId;
    }
    int getRequestCountPerMinute() {
        return requestCountPerMinute;
    }

    private String getStringProperty(Properties properties, String key) throws PluginCriticalException {
        isContainsKey(properties, key);
        String value = properties.getProperty(key);
        isBlank(key, value);
        return value;
    }

    private int getPositiveIntProperty(Properties properties, String key) throws PluginCriticalException {
        isContainsKey(properties, key);
        int value = tryParseInt(properties, key);
        isPositiveNumber(key, value);
        return value;
    }

    private void isContainsKey(Properties properties, String key) throws PluginCriticalException {
        if (!properties.containsKey(key)) {
            throw new PluginCriticalException(key + " field not found in config file.");
        }
    }

    private void isBlank(String key, String value) throws PluginCriticalException {
        if (value.isBlank()) {
            throw new PluginCriticalException(key + " field in config file not must be blank.");
        }
    }

    private int tryParseInt(Properties properties, String key) throws PluginCriticalException {
        try {
            return Integer.parseInt(properties.get(key).toString());
        } catch (NumberFormatException e) {
            throw new PluginCriticalException(key + " field value in config file must be integer number.");
        }
    }

    private void isPositiveNumber(String key, int value) throws PluginCriticalException {
        if (value <= 0) {
            throw new PluginCriticalException(key + " field value in config file must be positive integer number.");
        }
    }
}
