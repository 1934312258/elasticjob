package com.kevin.esjob.test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kevin.esjob.config.RestTemplateConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author zhaowenjian
 * @since 2021/5/19 15:43
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MapTestTest {

    @Autowired
    static RestTemplate restTemplate;

    @Autowired
    RedisTemplate redisTemplate;

    static String key = "key=1b337c9ff8b98c914fa4a5241ef1f5f0";
    static String original = "https://restapi.amap.com/v3/place/around?";
    static String secret = "952977f10a13ddd52bf34c5f5b534556";
    static Integer index = 301;
    // 当前页数
    static int page = 1;
    // 每页的数据量
    static int offset = 20;

    @Test
    public void testMap(){
        if(restTemplate == null){
            RestTemplateConfig config = new RestTemplateConfig();
            restTemplate = config.restTemplate();
        }
        String url = original + key + "&location=117.708408,39.023109&radius=50000&types=010100&offset=200&page_num=7";
//        url = "https://api.reapal.com/member/custmem/qryBalance?data=3OR7uLtVkYHmBZztn7IVKQopQkZxsCC9s2rBfHev6s0xGz%2BWso0loDrWSUIRvcvyZNA8XOcAD0XyNFJfxJfFYsc0OmbB0q3%2BDAU7WccfVwYP7vMw476yTy4QHYOjE2Qbq591dqOe6cclZ7tzEHucjchFqmEvXlbc9OZ7bXEsKw2aSu%2BXruTjLXfaRnSEtcLmUY9duco%2Fg3PDMFN3DbNiMHxppEQk%2B4TfFJRtjBolI1Vvl7rvVT22DzQ7oHMUUxMDX3%2BQMl2kYwH57TIQXE9R%2Fseq%2FnPh6e9RMrkyRan2mqo2BfeaqcSOwnqwoeMsYeordPUscGh3PoiedjdzsD42y7Pnka1Btr9WZ5WhA%2Ff1AJ0w%2FqjEZVbALru6wrWEBnb%2FFgeVfSLV2UuSKMRN7mENxz52TgIulC7qqJ2GoBxihCm6CSChpSsa%2FTCYRpnOP%2FBQR71VickYu3jL15FK1dy%2F86Lw3HqQMgm7%2BhywuMdBOCBdlkOtBJ83mS713K2S5hGlePnnerJpmpK0p46s%2BSnBuJ35S%2B1B3veqK7A2EXplucBe6XURyrRq4gd3GrxXZN6Y&merchant_id=100000000300001&encryptkey=BSkyOH34Owrqlcpq6OzeYZKtOqEGeTBKrJs%2B%2B8RCp4FbYnSiu%2FDVogkYTyd9sCoVvQpj6MP%2FshOPk9jGAgOiXR5SBbi%2Bio%2BlMytI5WjTfF1jxyeYGGRsUw%2FEo4kVBuYlSBwAAJBWvLmaSMF3umIrpd3PlN1LiJeJZtFnjqOT%2BzTrDFZ23DQo4CcM9icSmfLgyfuSyeahKpUp5O%2FM86T%2FKlG47HGaY1ma%2Bx8sw9F61W%2FW1%2FKQHD%2FZDm5RBvMH3RExijF9SbequwU8mhk%2FdHwmBSUKBCikHDcIYn77w3OBOSEBOsXD3bho2Qjv1j%2Bb9o2sC6oSIeaaCh%2BCGiRRlTqL%2BA%3D%3D";
        ResponseEntity<String> responseEntity = restTemplate.exchange(url+"12", HttpMethod.GET,null,String.class);
        responseEntity = restTemplate.exchange(url, HttpMethod.GET,null,String.class);
        /// 在执行获取response的过程中已经释放了连接到连接池，会用到EntityUtils.consume(response.getEntity())
//        for(int i =0;i<50;i++){
//            String finalUrl = url;
//            new Thread(()->{
//                restTemplate.exchange(finalUrl, HttpMethod.GET,null,String.class);
//            }).start();
//
//        }
//        restTemplate.exchange(url, HttpMethod.GET,null,String.class);
//        System.out.println();
//        for(int i =0;i<50;i++){
//            String finalUrl = url;
//            new Thread(()->{
//                restTemplate.exchange(finalUrl, HttpMethod.GET,null,String.class);
//                System.out.println("=====================================================================");
//            }).start();
//
//        }
//        restTemplate.exchange(url, HttpMethod.GET,null,String.class);
//        restTemplate.exchange(url, HttpMethod.GET,null,String.class);
        String result = responseEntity.getBody();
        Gson gson = new Gson();
        JsonObject original = gson.fromJson(result,JsonObject.class);
        JsonArray pois = original.getAsJsonArray("pois");
        JsonArray resultData = new JsonArray();
        Map<Integer, Point> map = new HashMap<>();
        pois.forEach(jsonObject->handlePoiResult(map, (JsonObject) jsonObject));
        redisTemplate.opsForGeo().add("kevin",map);
//        String lastResult = resultData.toString();
    }

    void handlePoiResult(Map<Integer, Point> map, JsonObject original){
//        JsonObject temp = new JsonObject();
//        temp.add("distance",original.get("distance"));
//        temp.add("location",original.get("location"));
//        resultData.add(temp);
        String str = original.get("location").getAsString();
        String[]strs = str.split(",");
        map.put(index++,new Point(new Double(strs[0]),new Double(strs[1])));
    }

    @Test
    public void testAddMap(){
        Map<Integer, Point> map = new HashMap<>();
        for(int i = 0; i<10000;i++){
            Map<String, String> jw = randomLonLat(80, 160, 20, 80);
            map.put(index++,new Point(new Double(jw.get("J")),new Double(jw.get("W"))));
        }
        redisTemplate.opsForGeo().add("kevin",map);
    }

    @Test
    public void testSearchMap(){
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().sortAscending();
        Circle circle = new Circle(new Point(new Double("117.00032"), new Double("39.0997")), new Distance(10000, RedisGeoCommands.DistanceUnit.METERS));
//        GeoResults<RedisGeoCommands.GeoLocation<Object>> results = redisService.radius(RedisKeyConstant.GAS_STATION_KEY, circle, args);
        Long start = System.currentTimeMillis();
        GeoResults<RedisGeoCommands.GeoLocation<Object>> results = redisTemplate.opsForGeo().radius("kevin",circle,args);
        System.out.println("调用redis耗时："+ (System.currentTimeMillis() - start));
        circle = new Circle(new Point(new Double("117.686765"), new Double("39.564334")), new Distance(10000, RedisGeoCommands.DistanceUnit.METERS));
        start = System.currentTimeMillis();
        results = redisTemplate.opsForGeo().radius("kevin",circle,args);
        System.out.println("第二次调用redis耗时："+ (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        results = redisTemplate.opsForGeo().radius("kevin",circle,args);
        System.out.println("第二次调用redis耗时："+ (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        results = redisTemplate.opsForGeo().radius("kevin",circle,args);
        System.out.println("第二次调用redis耗时："+ (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        results = redisTemplate.opsForGeo().radius("kevin",circle,args);
        System.out.println("第二次调用redis耗时："+ (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        String str = (String) redisTemplate.opsForValue().get("zhao");
        System.out.println("第二次调用redis耗时："+ (System.currentTimeMillis() - start));
    }

    /**
     * @Description: 在矩形内随机生成经纬度
     * @param MinLon：最小经度
     * 		  MaxLon： 最大经度
     *  	  MinLat：最小纬度
     * 		  MaxLat：最大纬度
     * @return @throws
     */
    public static Map<String, String> randomLonLat(double MinLon, double MaxLon, double MinLat, double MaxLat) {
        BigDecimal db = new BigDecimal(Math.random() * (MaxLon - MinLon) + MinLon);
        String lon = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();// 小数后6位
        db = new BigDecimal(Math.random() * (MaxLat - MinLat) + MinLat);
        String lat = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
        Map<String, String> map = new HashMap<String, String>();
        map.put("J", lon);
        map.put("W", lat);
        return map;
    }

}
