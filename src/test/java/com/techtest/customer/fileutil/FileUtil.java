package com.techtest.customer.fileutil;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class FileUtil {

    public String getStringFromFile(String fileName) {

        String result = StringUtils.EMPTY;
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            result = IOUtils.toString(classLoader.getResourceAsStream(fileName), "UTF-8");
        } catch (IOException e) {
            log.error("Error while reading the file in resources : {}", e);
        }
        return result;
    }

    public InputStream getInputStreamFromFile(String fileName) {

        String result = StringUtils.EMPTY;
        InputStream inputStream = null;
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            result = IOUtils.toString(classLoader.getResourceAsStream(fileName), "UTF-8");
            if (!result.equals(StringUtils.EMPTY)) {
                inputStream = IOUtils.toInputStream(result, "UTF-8");
            }
        } catch (IOException e) {
            log.error("Error while reading the file in resources : {}", e);
        }
        return inputStream;
    }
}
