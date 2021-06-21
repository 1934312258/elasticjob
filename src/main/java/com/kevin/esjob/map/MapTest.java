package com.kevin.esjob.map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import sun.security.provider.MD5;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * <p>
 *
 * </p>
 *
 * @author zhaowenjian
 * @since 2021/5/19 9:05
 */
public class MapTest {

    @Autowired
    static RestTemplate restTemplate;
    static String key = "key=1b337c9ff8b98c914fa4a5241ef1f5f0";
    static String original = "https://restapi.amap.com/v3/place/around?";
    static String secret = "952977f10a13ddd52bf34c5f5b534556";
    // 当前页数
    static int page = 1;
    // 每页的数据量
    static int offset = 20;

    static void handlePoiResult(JsonArray resultData,JsonObject original){
        JsonObject temp = new JsonObject();
        temp.add("distance",original.get("distance"));
        temp.add("location",original.get("location"));
        resultData.add(temp);
    }
    public static void main(String[] args) throws IOException {
        int a =0;
        if(a < 2 || a/0>1){
            System.out.println("1234");
        }
        CloseableHttpClient client = HttpClients.createDefault();
        String url = original + key + "&location=117.708408,39.023109&radius=50000&types=010100";
        HttpGet get = new HttpGet(url);
        for(int i =0;i<20;i++){
            client.execute(get);
        }
        CloseableHttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            // long len = entity.getContentLength();// -1 表示长度未知
            String result = EntityUtils.toString(entity);
            Gson gson = new Gson();
            JsonObject original = gson.fromJson(result,JsonObject.class);
            JsonArray pois = original.getAsJsonArray("pois");
            JsonArray resultData = new JsonArray();
            pois.forEach(jsonObject->handlePoiResult(resultData, (JsonObject) jsonObject));
            String lastResult = resultData.toString();
            response.close();
            System.out.println(lastResult);
        }
//        ResponseEntity responseEntity = restTemplate.exchange(url, HttpMethod.GET,null,String.class);
//        String str = (String) responseEntity.getBody();
        System.out.println(response);
    }
}
