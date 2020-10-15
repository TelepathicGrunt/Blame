package com.telepathicgrunt.blame.utils;

import java.util.HashMap;
import java.util.Map;

public class ErrorHints {

    public static final Map<String, String> HINT_MAP = createHintMap();

    private static Map<String, String> createHintMap() {
        Map<String,String> errorMap = new HashMap<>();
        errorMap.put("Not a json array", "This error typically means you need an certain kind of JSON array surrounding" +
                "\n   the current JSON. This is typically seen with worldgen JSON. If this is a worldgen JSON file, then" +
                "\n   check out slicedlime's example datapack for worldgen to find what's off: https://t.co/cm3pJcAHcy?amp=1");
        return errorMap;
    }
}
