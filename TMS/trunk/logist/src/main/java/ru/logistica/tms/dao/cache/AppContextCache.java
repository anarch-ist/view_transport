package ru.logistica.tms.dao.cache;

import ru.logistica.tms.dao.warehouseDao.RusTimeZoneAbbr;

import java.util.HashMap;
import java.util.Map;

public class AppContextCache {
    public static Map<RusTimeZoneAbbr, Double> timeZoneAbbrIntegerMap = new HashMap<>();
}
