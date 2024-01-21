package com.example.demo.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpService {

    private static HttpService instance = null;

    public static HttpService getInstance() {
        if (instance == null) {
            instance = new HttpService();
        }
        return instance;
    }

    public static String doGet(String url) {
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept","application/json");
        httpGet.setHeader("Content-type","application/json");
        try {
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //返回json格式
                String res = EntityUtils.toString(response.getEntity());
                return res;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
