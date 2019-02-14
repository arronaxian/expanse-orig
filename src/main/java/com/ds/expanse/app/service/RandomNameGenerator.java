package com.ds.expanse.app.service;

public class RandomNameGenerator {
    private final static String[] NAMES =
            {   "Arron", "Chris", "Joshua", "Clare", "Isabella", "Rafael",
                "Tom", "Gene", "Max", "Dax" };

    public static String build() {
        StringBuilder builder = new StringBuilder();
        builder.append(NAMES[(int)(Math.random() * NAMES.length)]);
        builder.append("_");
        for ( int index = 0; index < 10; index ++ ) {
            builder.append(Math.round(Math.floor(Math.random()*10.0)));
        }

        System.out.println("new player created " + builder.toString());

        return builder.toString();
    }
}
