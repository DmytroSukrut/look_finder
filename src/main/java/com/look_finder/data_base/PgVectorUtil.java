package com.look_finder.data_base;

public class PgVectorUtil {
    private PgVectorUtil() {}

    public static String float_to_string(float[] f) {
        if (f == null || f.length == 0) return "";

        StringBuffer sb = new StringBuffer();
        sb.append("[");

        for (int i = 0; i < f.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(Float.toString(f[i]));
        }
        sb.append("]");
        return sb.toString();
    }

}
