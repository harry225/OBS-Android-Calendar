package com.obs.mobile_tablet.wikid_utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by IntelliJ IDEA.
 * User: stguitar
 * Date: 4/5/12
 * Time: 12:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileUtils {
    public static String readStreamAsString(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        StringBuffer sb = new StringBuffer();
        while ( (line = reader.readLine() ) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }
}
