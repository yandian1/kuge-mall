package com.kuge.mall.warm.component;

import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.kuge.mall.common.utils.R;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * created by xbxie on 2024/8/10
 */
@Component
public class Task1 {

    private int getCode(String body) {
        R r = JSON.parseObject(body, R.class);
        return r.getCode();
    }

    private String adminToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoieGJ4aWUiLCJpZCI6MSwiYWNjb3VudCI6IjE4NzU1NzgxMDM5In0.hGnXrLOBmC-YbSZU_Ce5wMZf5rPd6hXqFUao6TR5P9g";

    private String memberToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoieGJ4aWUiLCJpZCI6MiwiYWNjb3VudCI6IjE4NzU1NzgxMDM5In0.-LMpj-lqoh6uNb21PNwuSZcIm6kW534PvMx-GoVlATQ";

    @Scheduled(fixedRate = 10000)
    public void admin() {
        send("admin", "admin", "http://localhost:8080/admin/admin/homepage/detail");
    }

    @Scheduled(fixedRate = 10000)
    public void auth() {
        send("auth", "admin", "http://localhost:8080/admin/auth/info/admin");
    }

    @Scheduled(fixedRate = 10000)
    public void product() {
        send("product", "member", "http://192.168.110.10:8080/app/product/homepage/detail");
    }

    @Scheduled(fixedRate = 10000)
    public void cart() {
        send("cart", "member", "http://192.168.110.10:8080/app/cart/cart/count");
        send("cart", "member", "http://192.168.110.10:8080/app/app/cart/cart/info");
    }

    private void send(String service, String app, String url) {

        HttpResponse res = HttpRequest
            .post(url)
            .header("authorization", Objects.equals(app, "admin") ? adminToken : memberToken)
            .execute();

        Console.log(service, getCode(res.body()));
    }
}
