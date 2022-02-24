package com.webserver.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class HttpContext {
    private static Map<String, String> mimeMapping = new HashMap<>();
    public static final char CR = 13;
    public static final char LF = 10;
    static{
        initMimeMapping();
    }
    private static void initMimeMapping() {
        Properties properties = new Properties();
        try {
            properties.load(
                    HttpContext.class.getResourceAsStream(
                            "./web.properties"
                    )
            );
            properties.forEach(
                    (k,v)->mimeMapping.put(k.toString(),v.toString())
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getMimeType(String ext) {
        return mimeMapping.get(ext);
    }
}
