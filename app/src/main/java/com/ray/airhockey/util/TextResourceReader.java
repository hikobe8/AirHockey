package com.ray.airhockey.util;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Author : hikobe8@github.com
 * Time : 2018/9/2 下午3:03
 * Description :
 */
public class TextResourceReader {

    public static String readTextFileFromResource(Context context, int resId) {
        StringBuilder body = new StringBuilder();
        try {
            InputStream inputStream = context.getResources().openRawResource(resId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String nextLine = null;
            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine);
                body.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("could not open resource : " + resId, e);
        } catch (Resources.NotFoundException e) {
            throw new RuntimeException("Resource not found : " + resId, e);
        }

        return body.toString();
    }

}
