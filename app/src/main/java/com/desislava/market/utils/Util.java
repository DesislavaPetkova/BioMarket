package com.desislava.market.utils;

/**
 * Parse JSON object
 */

public class Util {

    public static String getCategoryById(int category) {
        String cat = null;
        switch (category) {
            case 1:
                cat = "Fruits";
                break;
            case 2:
                cat = "Vegetables";
                break;
            case 3:
                cat = "Fresh Meat";
                break;
            case 4:
                cat = "Drinks";
                break;

        }

        return cat;

    }

}

