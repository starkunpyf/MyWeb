package com.webserver.http;

import java.util.HashMap;
import java.util.Map;

public class HttpContext {
    private static Map<String, String> mimeMapping = new HashMap<>();
    public static final char CR = 13;
    public static final char LF = 10;
    static{
        initMimeMapping();
    }
    private static void initMimeMapping() {
        mimeMapping.put("html","text/html");
        mimeMapping.put("css","text/css");
        mimeMapping.put("js","application/javascript");
        mimeMapping.put("gif","image/gif");
        mimeMapping.put("jpg","image/jpeg");
        mimeMapping.put("png","image/png");
    }
    public static String getMimeType(String ext) {
        return mimeMapping.get(ext);
    }
}
