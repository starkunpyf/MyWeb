package com.webserver.core;

import com.webserver.annotation.Controller;
import com.webserver.annotation.RequestMapping;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;




public class HandlerMapping {

    private static Map<String,MethodMapping> mappings = new HashMap<String,MethodMapping>();
    static{
        initMapping();
    }
    private static void initMapping() {
        try {
            File dir = new File(
                    DispatcherServlet.class.getClassLoader().getResource(
                            "./com/webserver/controller"
                    ).toURI()
            );
            File[] subs = dir.listFiles(f -> f.getName().endsWith(".class"));
            for (File sub : subs) {
                String fileName = sub.getName();
                String className = fileName.substring(0, fileName.indexOf("."));
                Class cls = Class.forName("com.webserver.controller." + className);

                //判断该类是否被@Controller标注了
                if (cls.isAnnotationPresent(Controller.class)) {
                    Object o = cls.newInstance();
                    Method[] methods = cls.getDeclaredMethods();
                    for (Method method : methods) {
                        //判断该方法是否被@RequestMapping标注了
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            //获取该注解
                            RequestMapping rm = method.getAnnotation(RequestMapping.class);
                            //获取该注解的参数(该方法处理的请求路径)
                            String path = rm.value();
                            MethodMapping methodMapping = new MethodMapping(o,method);
                            mappings.put(path,methodMapping);

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public static MethodMapping getMethod(String path){
        return mappings.get(path);
    }
    public static class MethodMapping{
        private Object controller;
        private Method method;

        public MethodMapping(Object controller, Method method) {
            this.controller = controller;
            this.method = method;
        }

        public Object getController() {
            return controller;
        }

        public Method getMethod() {
            return method;
        }
    }
}
