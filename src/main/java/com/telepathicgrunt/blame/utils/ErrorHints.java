package com.telepathicgrunt.blame.utils;

import java.util.HashMap;
import java.util.Map;

public class ErrorHints {

    public static final Map<String, String> HINT_MAP = createHintMap();

    private static Map<String, String> createHintMap() {
        Map<String,String> errorMap = new HashMap<>();

        errorMap.put("Not a json array",
                "\n     This error typically means you need an certain kind of JSON array surrounding" +
                "\n     the current JSON. This is typically seen with worldgen JSON. If this is a worldgen JSON file, then" +
                "\n     check out slicedlime's example datapack for worldgen to find what's off: https://t.co/cm3pJcAHcy?amp=1");

        errorMap.put("No key Properties in MapLike",
                "\n     This is usually caused by a datapack's or mod's worldgen JSON file" +
                "\n     trying to specify a blockstate but forgot to list ALL the block's properties." +
                "\n     Example of a correct blockstate JSON is the following: " +
                "\n        \"state\": {" +
                "\n          \"Properties\": {" +
                "\n             \"snowy\": \"false\"" +
                "\n           }," +
                "\n           \"Name\": \"minecraft:grass_block\"" +
                "\n        },");
        return errorMap;
    }
}
