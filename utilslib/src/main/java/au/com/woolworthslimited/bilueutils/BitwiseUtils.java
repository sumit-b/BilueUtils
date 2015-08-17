package au.com.woolworthslimited.bilueutils;

/**
 * Copyright (c) 2015 Woolworths. All rights reserved.
 */
public class BitwiseUtils {
    public static boolean isPermitted(int data, int permission) {
        return (data & permission) == permission;
    }

    public static int addPermission(int data, int permission) {
        data |= permission;
        return data;
    }

    public static int deletePermission(int data, int permission) {
        data &= ~permission;
        return data;
    }
}
