package com.telepathicgrunt.blame.utils;

import java.util.HashMap;
import java.util.Map;

public class ErrorHints {

    public static final Map<String, String> HINT_MAP = createHintMap();

    private static Map<String, String> createHintMap() {
        Map<String, String> errorMap = new HashMap<>();

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

        errorMap.put("Base value out of range",
                "\n     This error means that a value was given that was either too large or too small." +
                "\n     The allowed range should be specified below in \"Prettified form of the broken JSON:\"" +
                "\n     The two values are separated by a - dash. That means -10-256 means between -10 and 256." +
                "\n     Let the modder know to correct their out-of-bounds value.");

        errorMap.put("Unknown registry key",
                "\n     An key that was suppose to point to a registry entry was unable to find the entry." +
                        "\n     Contact the modder so that they can check to see they didn't typo the below" +
                        "\n     resourcelocation or forgot to register their stuff.");

        return errorMap;
    }
}
