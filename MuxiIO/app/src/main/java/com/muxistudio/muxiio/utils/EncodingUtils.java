package com.muxistudio.muxiio.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by kolibreath on 17-8-23.
 */

public class EncodingUtils {
    public static String encodePassword(String password) throws UnsupportedEncodingException {
        return Base64.encodeToString(password.getBytes("utf-8"),Base64.DEFAULT);
    }
}
