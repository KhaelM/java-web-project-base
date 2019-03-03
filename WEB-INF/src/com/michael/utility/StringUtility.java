package com.michael.utility;


/**
 * StringUtility
 */
public class StringUtility {

    public static String firstUpper(String string, boolean isRestLower) {
        if(isRestLower) {
            return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
        }
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static String fromCamelCaseToUnderscore(String string) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        String result = string.replaceAll(regex, replacement).toLowerCase();

        return result;
    }

    public String fromUnderscoreToPascalCase(String string) {
        if (string.contains("_")) {
            String[] splitted = string.split("_");
            string = firstUpper(splitted[0], true);

            for (int i = 1; i < splitted.length; i++) {
                string += firstUpper(splitted[i], true);
            }
        }

        return string;
    }

    public static String fromUnderscoreToCamelCase(String string) {
        if (string.contains("_")) {
            String[] splitted = string.split("_");
            string = splitted[0].toLowerCase();

            for (int i = 1; i < splitted.length; i++) {
                string += firstUpper(splitted[i], true);
            }
        }

        return string;
    }
}