package org.common;

import java.io.IOException;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import static util.FileUtil.writeTmpFile;

public class Logging {
    public static Logger logger = Logger.getLogger("Logging");
    public static FileHandler fileHandler;

    static {
        try {
            //TODO we can use profile to set the filename
            fileHandler = new FileHandler("./output.log");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Logging constructor
    public Logging() {
        logger.addHandler(this.fileHandler);
    }

    public void setFileHandler(FileHandler fileHandler) {
            this.fileHandler = fileHandler;
    }

    /***
     * This method is used to write the method hashmap in a file
     * @param map
     */
    public void hashToFile(Map<String,String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        if(map == null || map.isEmpty()) {
            return;
        }
        for(Map.Entry<String,String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + " " + value);
            stringBuilder.append(key + " " + value + "\n");
        }
        writeTmpFile("methodsCollection", ".log", stringBuilder.toString());
    }

    /***
     * This method is used to log the method calls in a file
     * @param msg
     */
    public void logToFile(String msg) {
        try {
            logger.info(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
