package org;

import net.bytebuddy.asm.Advice;
import org.common.Logging;

import java.util.HashMap;


public class CollectMethods {

    public static HashMap<String,String> hashMap = new HashMap<>();

    public static Logging logging = new Logging();
    public CollectMethods() {
        System.out.println("CollectMethods init");
    }
    @Advice.OnMethodEnter
    public static long enter(@Advice.Origin String method) {

        long start = System.currentTimeMillis();
        return start;
    }

    @Advice.OnMethodExit
    public static void exit(@Advice.Origin String method,@Advice.Enter long start) {

        if(method == null || method.isEmpty()) {
            return;
        }
        String className = method.split("\\(")[1];

        try{
            if (className != null
                    && !className.isEmpty())
            {
                if (!className.startsWith("java") && !className.startsWith("jdk")) {
                    return;
                }
                className = className.split("\\)")[0];
                if (hashMap.containsKey(className)) {
                    return;
                }
                long end = System.currentTimeMillis();
                hashMap.put(className, className);
                logging.logToFile(className + " took " + (end - start) + " milliseconds " + "size: " + hashMap.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static HashMap<String,String> getHashMap() {
        return hashMap;
    }
}
