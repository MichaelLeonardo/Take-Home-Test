package com.example.takehometest.utils;

public class Helper {

    public static String getCamelCaseName(String orderBy) {
        // Remove underscore and convert to camel case
        // for first character do not convert to uppercase
        String[] words = orderBy.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                sb.append(word);
            } else {
                sb.append(word.substring(0, 1).toUpperCase()).append(word.substring(1));
            }
        }
        return sb.toString();
    }
}
