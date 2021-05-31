package com.telepathicgrunt.blame.utils;


/*
 * Creator: Christian Morin
 * Licensed under: CC BY-SA 3.0
 * Source: https://stackoverflow.com/a/24605993
 */
public class PrettyPrintBrokenJSON {

    public static String prettyPrintJSONAsString(String jsonString) {

        int tabCount = 0;
        StringBuffer prettyPrintJson = new StringBuffer();
        String lineSeparator = "\r\n";
        String tab = "  ";
        boolean ignoreNext = false;
        boolean inQuote = false;

        char character;

        /* Loop through each character to style the output */
        for (int i = 0; i < jsonString.length(); i++) {

            character = jsonString.charAt(i);

            if (inQuote) {

                if (ignoreNext) {
                    ignoreNext = false;
                }
                else if (character == '"') {
                    inQuote = false;
                }
                prettyPrintJson.append(character);
            }
            else {

                if (ignoreNext) {
                    ignoreNext = !ignoreNext;
                }

                switch (character) {

                    case '[':
                    case '{':
                        ++tabCount;
                        prettyPrintJson.append(character);
                        prettyPrintJson.append(lineSeparator);
                        printIndent(tabCount, prettyPrintJson, tab);
                        break;

                    case ']':
                    case '}':
                        --tabCount;
                        prettyPrintJson.append(lineSeparator);
                        printIndent(tabCount, prettyPrintJson, tab);
                        prettyPrintJson.append(character);
                        break;

                    case '"':
                        inQuote = true;
                        prettyPrintJson.append(character);
                        break;

                    case ',':
                        prettyPrintJson.append(character);
                        prettyPrintJson.append(lineSeparator);
                        printIndent(tabCount, prettyPrintJson, tab);
                        break;

                    case ':':
                        prettyPrintJson.append(character).append(" ");
                        break;

                    case '\\':
                        prettyPrintJson.append(character);
                        ignoreNext = true;
                        break;

                    default:
                        prettyPrintJson.append(character);
                        break;
                }
            }
        }

        return prettyPrintJson.toString();
    }

    private static void printIndent(int count, StringBuffer stringBuffer, String indent) {
        for (int i = 0; i < count; i++) {
            stringBuffer.append(indent);
        }
    }
}
