package com.joeys.router.runtime;

import android.os.Bundle;

public class BundleUtils {

    public static <T> T get(Bundle bundle, String key) {

        return (T) bundle.get(key);
    }


    public static <T> T get(Bundle bundle, String key, Object defaultValue) {

        Object o = bundle.get(key);
        if (o == null) return (T) defaultValue;
        else return (T) o;

    }


}
